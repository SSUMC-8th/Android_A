package com.example.floclone

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.floclone.database.Album
import com.example.floclone.database.AlbumDatabase
import com.example.floclone.database.SongDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job

import com.example.floclone.database.Song as SongDB

class MainActivity : AppCompatActivity() {

    private lateinit var clHomeplayer : ConstraintLayout
    private var playCheck = false;

    //코루틴으로 Media재생 및 넘겨주기
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private var updateJob: Job? = null
    private var currentPosition: Int = 0
    private var durationSong: Int = 0

    private lateinit var btnPlay : ImageButton
    private lateinit var btnPause : ImageButton
    private lateinit var btnPrevious : ImageButton
    private lateinit var btnNext : ImageButton

    lateinit var tvTitle : TextView
    lateinit var tvArtist : TextView

    private var songId: Int = 0
    private var songList: ArrayList<SongDB> = arrayListOf()
    private var nowPos: Int = 0

    //registerForActivityResult
    private val songResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val returnString = result.data?.getStringExtra("title")
            Toast.makeText(this, "Title: $returnString", Toast.LENGTH_SHORT).show()

            //songActivity에서 받아온 거 연동
            val returnedPosition = result.data?.getIntExtra("currentPosition", 0) ?: 0
            val returnedPlayCheck = result.data?.getBooleanExtra("playCheck", false) ?: false

            //연동하기
            currentPosition = returnedPosition
            seekBar.progress = currentPosition
            mediaPlayer.seekTo(currentPosition)

            playCheck = returnedPlayCheck
            if(playCheck) {
                playMusic()
                btnPlay.visibility = View.GONE
                btnPause.visibility = View.VISIBLE
            }
            else{
                stopMusic()
                btnPlay.visibility = View.VISIBLE
                btnPause.visibility = View.GONE
            }

            //SongActivity UI 연동
            val sharedPref = getSharedPreferences("Song", MODE_PRIVATE)
            songId = sharedPref.getInt("songId", 1)
            for(i in 0 until songList.size){
                if(songList.get(i).id == songId){nowPos = i}
            }
            tvTitle.text = songList.get(nowPos).title
            tvArtist.text = songList.get(nowPos).singer


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //DB랑 sharedPreference에서 가져오기
        initSong()
        initPlayList()

        tvTitle = findViewById<TextView>(R.id.tv_homeplayer_title)
        tvArtist = findViewById<TextView>(R.id.tv_homeplayer_artist)

        var title = tvTitle.text.toString()
        var artist = tvArtist.text.toString()

        btnPlay = findViewById<ImageButton>(R.id.btn_homeplayer_play);
        btnPause = findViewById<ImageButton>(R.id.btn_homeplayer_pause);
        btnPrevious = findViewById<ImageButton>(R.id.btn_homeplayer_previous)
        btnNext = findViewById<ImageButton>(R.id.btn_homeplayer_next)

        //홈 뮤직 정의
        handleMusic()


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.ct_home, HomeFragment())
                .commit()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnv_home)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                //홈 화면
                R.id.navigation_homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.ct_home, HomeFragment())
                        .commit()
                    true
                }
                //보관함
                R.id.navigation_storeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.ct_home, LockerFragment())
                        .commit()
                    true
                }
                R.id.navigation_lookaroundFragment ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.ct_home, LookaroundFragment())
                        .commit()
                    true
                }
                // 다른 메뉴 항목에 대해서도 필요하면 추가
                else -> false
            }
        }

        btnPlay.setOnClickListener {
            btnPlay.visibility = View.GONE
            btnPause.visibility = View.VISIBLE
            playCheck = true
            playMusic()
        }
        btnPause.setOnClickListener {
            btnPlay.visibility = View.VISIBLE
            btnPause.visibility = View.GONE
            playCheck = false
            stopMusic()
        }
        btnPrevious.setOnClickListener {
            songId -= 1
            if(songId < 1){ //최소 번호 1번으로 갈 경우 1로 고정
                songId = 1
            }

            for(i in 0 until songList.size) {
                if (songList.get(i).id == songId) {
                    nowPos = i
                }
            }

            tvTitle.text = songList.get(nowPos).title
            tvArtist.text = songList.get(nowPos).singer
        }
        btnNext.setOnClickListener {
            songId += 1
            if(songId > songList.size){
                songId = songList.size
            }

            for(i in 0 until songList.size) {
                if (songList.get(i).id == songId) {
                    nowPos = i
                }
            }

            tvTitle.text = songList.get(nowPos).title
            tvArtist.text = songList.get(nowPos).singer
        }

        //플레이 바랑 연결해서 노래 액티비티로 이동
        clHomeplayer = findViewById(R.id.cl_homeplayer)
        clHomeplayer.setOnClickListener({
            //코루틴 정리
            if(mediaPlayer.isPlaying){
                stopMusic()
            }
            updateJob?.cancel()

            //sharedPreference에 현재 songID 저장
            val sharedPref = getSharedPreferences("Song", MODE_PRIVATE)
            sharedPref.edit().putInt("songId", songId).apply()

            //intent 처리
            val intent = Intent(this, SongActivity::class.java)
            val songPlay = SongPlay(title, artist, playCheck,
                currentPosition, durationSong,
                "例えば僕ら二人 煌めく映画のように",
                "出会いなおせたらどうしたい")
            intent.putExtra("songPlay", songPlay)
            //intent를 만들고 startActivity 대신 registerForActivityResult를 수행
            songResultLauncher.launch(intent)
        })

        //DB에 넣을 때
        //inputSongs()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    //fragment에서 activity 내용을 바꿀 수 있도록 함수 정의
    fun updateMiniplayerString(title: String, artist: String){
        tvTitle.text = title
        tvArtist.text = artist
    }


    private fun handleMusic(){
        mediaPlayer = MediaPlayer.create(this, R.raw.lady_kenshi_yonezu)
        seekBar = findViewById<SeekBar>(R.id.sbar_HomeActivity)
        seekBar.max = mediaPlayer.duration
        durationSong = mediaPlayer.duration
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // 사용자가 직접 조작했다면 미디어 플레이어 위치 변경
                if(fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // 사용자가 터치를 시작할 때 필요한 동작을 구현
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // 사용자가 터치를 종료할 때 필요한 동작을 구현
            }
        })
    }
    
    private fun playMusic(){
        if(!mediaPlayer.isPlaying) {
            mediaPlayer.start() //재생
        }
        updateSeekbar()
    }

    private fun stopMusic(){
        if(mediaPlayer.isPlaying) {
            mediaPlayer.pause() //멈춤
        }
    }

    //코루틴으로 updateSeekbar 설정
    private fun updateSeekbar(){
        //코루틴 이미 실행중이면 더 실행 X
        if(updateJob?.isActive == true) return

        updateJob = lifecycleScope.launch(Dispatchers.Main){
            while(mediaPlayer.isPlaying){
                delay(1000)
                //만약 플레이 멈추면 이 코루틴 종료
                if(!mediaPlayer.isPlaying) break

                currentPosition = mediaPlayer.currentPosition
                seekBar.progress = currentPosition
            }
        }

    }

    //현재 노래의 ID를 가져오기(Sharedpreference)
    private fun initSong(){
        //sharedPreferences 적용 및 가져오기
        val sharedPref = getSharedPreferences("Song", MODE_PRIVATE)

        //값 넣기
        //sharedPref.edit().putInt("songId", 1).apply()
        songId = sharedPref.getInt("songId", 1)
    }
    //현재 DB에서 노래들 가져오기(RoomDB)
    private fun initPlayList(){
        val dao = SongDatabase.getDatabase(this).songDao()

        lifecycleScope.launch{
            //DB에서 모든 노래들 가져오기
            val songs = dao.getAllSongs()
            val songsList = ArrayList(songs)

            //이를 json 형태로 바꾸기
            val gson = Gson()
            val json = gson.toJson(songsList)

            //sharedPreference에 저장
            val sharedPref = getSharedPreferences("Song", MODE_PRIVATE)
            sharedPref.edit().putString("songList", json).apply()

            
            //sharedPreference에서 가져오기(json -> ArrayList)
            val getJson = sharedPref.getString("songList", "")
            val gson2 = Gson()
            val type = object : TypeToken<ArrayList<SongDB>>() {}.type
            songList = gson2.fromJson(getJson, type)

            //UI 작업
            for(i in 0 until songList.size){
                if(songList.get(i).id == songId){
                    nowPos = i
                }
        }


            tvTitle.text = songList.get(nowPos).title
            tvArtist.text = songList.get(nowPos).singer

        }

    }

    //temp(DB에 노래 저장)
    private fun inputSongs(){
        val db = SongDatabase.getDatabase(this)
        val dao = db.songDao()

        val db2 = AlbumDatabase.getDatabase(this)
        val dao2 = db2.albumDao()



        val albumList = listOf<Album>(
            Album(title = "Lost corner", singer = "Kenshi Yonezu", coverImg = R.drawable.lostcorner),
            Album(title = "The book 3", singer = "Yoasobi", coverImg = R.drawable.thebook3),
            Album(title = "愛を伝えたいだとか", singer = "Aimyon", coverImg = R.drawable.aiwotsutaetaidatoka),
            Album(title = "Digital single", singer = "Kenshi Yonezu", coverImg = R.drawable.lostcorner)
        )


        val songlist = listOf<SongDB>(
            SongDB(title = "Lady", singer = "Kenshi Yonezu", second = 240, playTime = 0, isPlaying = false, music = "Lady.mp3", coverImg = R.drawable.album_lady, isLike = false, albumIdx = 1),
            SongDB(title = "愛を伝えたいだとか", singer = "Aimyon", second = 240, playTime = 0, isPlaying = false, music = "愛を伝えたいだとか.mp3", coverImg = R.drawable.aiwotsutaetaidatoka, isLike = false, albumIdx = 3),
            SongDB(title = "勇者", singer = "Yoasobi", second = 240, playTime = 0, isPlaying = false, music = "勇者.mp3", coverImg = R.drawable.thebook3, isLike = false, albumIdx = 2),
            SongDB(title = "群青", singer = "Yoasobi", second = 240, playTime = 0, isPlaying = false, music = "群青.mp3", coverImg = R.drawable.thebook, isLike = false, albumIdx = 2),
            SongDB(title = "Spinning Globe", singer = "Kenshi Yonezu", second = 240, playTime = 0, isPlaying = false, music = "Spinning Globe.mp3", coverImg = R.drawable.lostcorner, isLike = false, albumIdx = 1),
            SongDB(title = "Pop Song", singer = "Kenshi Yonezu", second = 240, playTime = 0, isPlaying = false, music = "Pop Song.mp3", coverImg = R.drawable.lostcorner, isLike = false, albumIdx = 1),
            SongDB(title = "毎日", singer = "Kenshi Yonezu", second = 240, playTime = 0, isPlaying = false, music = "毎日.mp3", coverImg = R.drawable.lostcorner, isLike = false, albumIdx = 1),
            SongDB(title = "BOW AND ARROW", singer = "Kenshi Yonezu", second = 240, playTime = 0, isPlaying = false, music = "BOW AND ARROW.mp3", coverImg = R.drawable.bowandarrow, isLike = false, albumIdx = 4),
            SongDB(title = "Plazma", singer = "Kenshi Yonezu", second = 240, playTime = 0, isPlaying = false, music = "Plazma.mp3", coverImg = R.drawable.plazma, isLike = false, albumIdx = 4),
            SongDB(title = "アイドル", singer = "Yoasobi", second = 240, playTime = 0, isPlaying = false, music = "アイドル.mp3", coverImg = R.drawable.thebook3, isLike = false, albumIdx = 2)
        )

        lifecycleScope.launch {
            songlist.forEach { song->
                //dao.updateCoverImgByTitle(song.title, song.coverImg ?: 0)
                Log.d("tagcheck", "{${song.title}}: ${song.coverImg}")
            }
            albumList.forEach { album->
                dao2.updateCoverImgByTitle(album.title, album.coverImg ?: 0)
                Log.d("tagcheck", "${album.title} ${album.coverImg}")
            }
        }

    }

    //lockerFragment에서 bottomDialog 띄우기 위한 코드
    fun showBottomActionBar() {
        val bar = findViewById<LinearLayout>(R.id.ll_bottomsheetdialog)
        val bottombar = findViewById<BottomNavigationView>(R.id.bnv_home)
        bar.visibility = View.VISIBLE
        bottombar.visibility = View.INVISIBLE
        bar.animate().translationY(0f).setDuration(300).start()
    }

    fun hideBottomActionBar() {
        val bar = findViewById<LinearLayout>(R.id.ll_bottomsheetdialog)
        val bottombar = findViewById<BottomNavigationView>(R.id.bnv_home)
        bar.animate().translationY(bar.height.toFloat()).setDuration(300)
            .withEndAction { bar.visibility = View.GONE }
            .start()
        bottombar.visibility = View.VISIBLE
    }


    //바로 다시 실행
    override fun onResume() {
        super.onResume()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnv_home)

        if(bottomNavigationView.selectedItemId == R.id.navigation_storeFragment){
            supportFragmentManager.beginTransaction()
                .replace(R.id.ct_home, LockerFragment())
                .commit()
        }


    }


}