package com.example.floclone

import android.media.MediaPlayer
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.floclone.database.SongDatabase
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import com.example.floclone.database.Song as SongDB

class SongActivity : AppCompatActivity() {

    private lateinit var arrowDownButton : ImageButton
    private var returnString: String = "제목"
    var playCheck = true;
    private var currentPosition: Int = 0
    private var durationSong: Int = 0

    //노래 추가
    private var songId : Int = 0
    private lateinit var songList : ArrayList<SongDB>
    private var nowPos : Int = 0
    //여기서 Firestore의 isLkie추가
    private lateinit var likeSongList : ArrayList<SongDB>

    //노래 재생을 위한 정의
    private lateinit var btnPlay : ImageButton
    private lateinit var btnPause : ImageButton
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var startString : TextView
    private lateinit var endString : TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private var updateThread: Thread? = null
    private lateinit var btnFavorite : ImageButton
    private lateinit var imvHeartbbyong : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_song)

        btnFavorite = findViewById<ImageButton>(R.id.btn_favorite_songActivity)
        imvHeartbbyong = findViewById<ImageView>(R.id.imv_heartbbyong_songActivity)

        //sharedPreference로 전달 데이터(노래 전체 Songlist)
        val sharedPref = getSharedPreferences("Song", MODE_PRIVATE)
        songId = sharedPref.getInt("songId", 1)
        val Json = sharedPref.getString("songList", "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<SongDB>>() {}.type
        songList = gson.fromJson(Json, type)
        likeSongList = ArrayList()

        //Firebase를 이용해 isLike된 노래들을 가져오기 + likedSonglist에 넣기
        //맨 처음에는 songList에다가 firestore에서 가져온 것들을 쭈욱 IN
        getLikedSongFromFirestore()


        for(i in 0 until songList.size){
            if(songList.get(i).id == songId){
                nowPos = i
            }
        }

        //전달 데이터
        val songPlay = intent.getParcelableExtra<SongPlay>("songPlay")
        playCheck = songList.get(nowPos).isPlaying
        currentPosition = songPlay!!.currentPosition
        durationSong = songPlay.durationSong


        //만약 현재 노래가 isLikedSong에 있으면 OK ---> 이를 조금 이따가 옮기자(밑의 getLikedSongFromFirestore 부분으로)
        //if(songList.get(nowPos).isLike){btnFavorite.setImageResource(R.drawable.ic_my_like_on)}
        //else{btnFavorite.setImageResource(R.drawable.ic_my_like_off)}

        findViewById<TextView>(R.id.tv_songName_songActivity).text = songList.get(nowPos).title
        findViewById<TextView>(R.id.tv_artistName_songActivity).text = songList.get(nowPos).singer
        findViewById<TextView>(R.id.tv_lyric1_songActivity).text = "가사 1"
        findViewById<TextView>(R.id.tv_lyric2_songActivity).text = "가사 2"
        findViewById<ImageView>(R.id.imv_albumCover_songActivity).setImageResource(songList.get(nowPos).coverImg ?: R.drawable.gibonsong)
        returnString = songList.get(nowPos).title

        //UI 연결 작업
        initUI()

        //MediaPlayer에 노래를 연결하고 seekbar에도 적용
        mediaPlayer = MediaPlayer.create(this, R.raw.lady_kenshi_yonezu)
        seekBar.max = mediaPlayer.duration
        endString.text = milliTotime(mediaPlayer.duration)

        //받아온 걸로 다시
        seekBar.max = durationSong
        endString.text = milliTotime(durationSong)
        mediaPlayer.seekTo(currentPosition)
        startString.text = milliTotime(currentPosition)
        

        //처음 재생버튼 초기화
        checkPlay()

        /** 버튼 listenr 정의 시작 **/
        //돌아가기버튼
        arrowDownButton.setOnClickListener({
            returnHome()
        })
        //재생/멈춤
        btnPlay.setOnClickListener {
            playCheck = true;
            checkPlay()

        }
        btnPause.setOnClickListener {
            playCheck = false;
            checkPlay()
        }
        btnPrevious.setOnClickListener {resetSong(1)}
        btnNext.setOnClickListener {resetSong(2)}
        btnFavorite.setOnClickListener { handleFavorite() }
        /** 버튼 listener 정의 끝 **/

        //Seekbar를 눌러서 변화할 때 Listener
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            //드래그 중
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    //변경
                    mediaPlayer.seekTo(progress)
                    startString.text = milliTotime(progress)
                }
            }
            //최초 탭 드래그 시 발생
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            //드래그 멈추면 발생
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        //뒤로가기 눌렀을 때 처리
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                returnHome()
            }
        })
        
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun initUI(){
        btnPlay = findViewById<ImageButton>(R.id.btn_play_songActivity)
        btnPause = findViewById<ImageButton>(R.id.btn_pause_songActivity)
        btnNext = findViewById<ImageButton>(R.id.btn_next_songActivity)
        btnPrevious = findViewById<ImageButton>(R.id.btn_previous_songActivity)
        arrowDownButton = findViewById(R.id.btn_arrowDown_songActivity)
        startString = findViewById<TextView>(R.id.tv_startmusic_songActivity)
        endString = findViewById<TextView>(R.id.tv_endmusic_songActivity)
        seekBar = findViewById<SeekBar>(R.id.sbar_songActivity)

    }

    //재생 상태에 따라 UI 변경
    private fun checkPlay(){
        if(playCheck){
            btnPlay.visibility = View.GONE
            btnPause.visibility = View.VISIBLE
            if(!mediaPlayer.isPlaying) {
                mediaPlayer.start() //재생
            }
            updateSeekbar() //seekbar 업데이트
        } else{
            btnPlay.visibility = View.VISIBLE
            btnPause.visibility = View.GONE
            if(mediaPlayer.isPlaying) {
                mediaPlayer.pause() //멈춤
            }
        }
    }

    //1초마다 seekbar update
    private fun updateSeekbar(){
        // 이미 스레드가 실행 중이면 새로 시작하지 않음
        if (updateThread != null && updateThread!!.isAlive) return

        updateThread = Thread{
            while(mediaPlayer.isPlaying) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    // 스레드가 인터럽트되면 종료
                    break
                }
                if(!mediaPlayer.isPlaying){break}
                else if (mediaPlayer.isPlaying) {
                    //설정
                    runOnUiThread {
                        currentPosition = mediaPlayer.currentPosition
                        seekBar.progress = currentPosition
                        startString.text = milliTotime(currentPosition)
                    }
                }
            }
        }

        updateThread?.start()
    }

    //일단 누르면 정지(mode = 1 (이전) / mode = 2 (다음)
    private fun resetSong(mode : Int){
        mediaPlayer.seekTo(0) //0이동
        seekBar.progress = 0 //seekbar도 0
        startString.text = milliTotime(0) //text도 0

        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            playCheck = false
            checkPlay()
        }

        //UI 작업
        if(mode == 1){
            songId -= 1
            if(songId < 1){ //최소 번호 1번으로 갈 경우 1로 고정
                songId = 1
            }
        }
        else{
            songId += 1
            if(songId > songList.size){
                songId = songList.size
            }
        }

        for(i in 0 until songList.size) {
            if (songList.get(i).id == songId) {
                nowPos = i
            }
        }

        findViewById<TextView>(R.id.tv_songName_songActivity).text = songList.get(nowPos).title
        findViewById<TextView>(R.id.tv_artistName_songActivity).text = songList.get(nowPos).singer
        findViewById<ImageView>(R.id.imv_albumCover_songActivity).setImageResource(songList.get(nowPos).coverImg ?: R.drawable.gibonsong)

        if(songList.get(nowPos).isLike){btnFavorite.setImageResource(R.drawable.ic_my_like_on)}
        else{btnFavorite.setImageResource(R.drawable.ic_my_like_off)}

    }

    // 밀리초를 "분:초" 형식으로 변환하는 함수
    private fun milliTotime(milliseconds: Int): String {
        val seconds = (milliseconds / 1000) % 60 //밀리세컨트*1000 = 초(총 몇 초)
        val minutes = (milliseconds / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun returnHome(){
        val sharedPref = getSharedPreferences("Song", MODE_PRIVATE)
        sharedPref.edit().putInt("songId", songId).apply()

        val resultIntent = Intent()
        resultIntent.putExtra("title", returnString)
        //추가로 재생 정보
        resultIntent.putExtra("currentPosition", currentPosition)
        resultIntent.putExtra("playCheck", playCheck)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
    
    //좋아요 싫어요 버튼 눌렀을 때, songList에 반영 + DB에 업데이트
    private fun handleFavorite(){
        //추가 Firestore!!
        val sharedPrefLogin = getSharedPreferences("login", MODE_PRIVATE)
        val loginCheck = sharedPrefLogin.getBoolean("loginCheck", false)
        val uid = sharedPrefLogin.getString("id", null)

        if(loginCheck && uid != null) {
            val dbLike = FirebaseDatabase.getInstance().getReference("Like").child(uid).child("song")

            if (songList.get(nowPos).isLike) {
                //없앤다
                val nSong = songList.get(nowPos)
                val sid = "song_${nSong.id}"
                dbLike.child(sid).removeValue()
            } else {
                //넣는다
                val nSong = songList.get(nowPos)
                val sid = "song_${nSong.id}"
                dbLike.child(sid).setValue(nSong)
            }


            //songList에 반영
            if (songList.get(nowPos).isLike) {
                songList.get(nowPos).isLike = false
            } else {
                songList.get(nowPos).isLike = true

                //Heart View를 보여주기 (그냥 Pop Animation 만들어서 띄우기)
                //총 1초 동안 실행
                imvHeartbbyong.apply {
                    alpha = 0f //투명도 0
                    visibility = TextView.VISIBLE
                    animate()
                        .alpha(1f) //투명도 0->1로 보이게
                        .setDuration(300) //약 0.3초간 실행(0->1 시간)
                        .withEndAction { //이거 끝나면
                            animate()
                                .alpha(0f) //다시 투명하게 하기
                                .setDuration(300) //약 0.3초간 1->0 으로 바꾸기
                                .setStartDelay(400) //약 0.4초간 기다렸다가 실행
                                .withEndAction {
                                    visibility = TextView.GONE
                                }
                        }
                }

            }

            //UI 반영
            if(songList.get(nowPos).isLike){btnFavorite.setImageResource(R.drawable.ic_my_like_on)}
            else{btnFavorite.setImageResource(R.drawable.ic_my_like_off)}

        }


    }

    //firestore에서 가져오기
    private fun getLikedSongFromFirestore(){
        //Firebase에서 유저 별로 isLike노래들 가져오기
        val sharedPrefLogin = getSharedPreferences("login", MODE_PRIVATE)
        val loginCheck = sharedPrefLogin.getBoolean("loginCheck", false)
        val uid = sharedPrefLogin.getString("id", null)

        if(loginCheck && uid != null){
            val dbLike = FirebaseDatabase.getInstance().getReference("Like").child(uid).child("song")
            dbLike.get().addOnSuccessListener { snapshot ->
                //uid가 가진 key들을 얻는다.
                for (child in snapshot.children) {
                    Log.d("tagcheck", "일단 1차: ${child.value}")
                    val map = child.value as? Map<String, Any>
                    map?.let {
                        Log.d("tagcheck", "map? 됬나?")
                        val song = SongDB(
                            id = (it["id"] as Long).toInt(),
                            title = it["title"] as String,
                            singer = it["singer"] as String,
                            second = (it["second"] as Long).toInt(),
                            playTime = (it["playTime"] as Long).toInt(),
                            isPlaying = it["playing"] as Boolean,
                            music = it["music"] as String,
                            coverImg = (it["coverImg"] as Long).toInt(),
                            isLike = it["like"] as Boolean,
                            albumIdx = (it["albumIdx"] as Long).toInt()
                        )
                        likeSongList.add(song)
                    }
                }
                updateLikeStatusInSongList()
            }
                .addOnFailureListener {
                    //
                }
        }


    }

    private fun updateLikeStatusInSongList(){
        for(song in songList){
            val matched = likeSongList.any {it.title.equals(song.title) && it.singer.equals(song.singer)}
            song.isLike = matched
        }


        //UI 작업
        if(songList.get(nowPos).isLike){btnFavorite.setImageResource(R.drawable.ic_my_like_on)}
        else{btnFavorite.setImageResource(R.drawable.ic_my_like_off)}

    }
    

    //노래 멈추기
    override fun onDestroy() {
        super.onDestroy()
        updateThread?.interrupt()
        if(mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}