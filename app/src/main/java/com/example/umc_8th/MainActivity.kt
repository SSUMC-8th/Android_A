package com.example.umc_8th

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivityMainBinding
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.room.Room

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var songActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var songs: List<Song>
    private var nowPos = 0

    private val progressReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val progress = intent?.getIntExtra("currentPosition", 0) ?: 0
            Log.d("ProgressReceiver", "Received progress: $progress")
            binding.mainplayerSb.progress = progress
        }
    }

    private val albumPlayReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val title = intent?.getStringExtra("title") ?: return
            val artist = intent.getStringExtra("artist") ?: return
            val progress = intent.getIntExtra("progress", 0)
            val duration = intent.getIntExtra("duration", 100)

            binding.mainplayerTitle.text = title
            binding.mainplayerArtist.text = artist
            binding.mainplayerSb.max = duration
            binding.mainplayerSb.progress = progress


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadAllSongs()
        initSongFromPrefs() // 초기화

        // BroadcastReceiver 등록 (onCreate에서 항상 등록)
        registerReceiver(albumPlayReceiver, IntentFilter("com.example.umc_8th.ALBUM_PLAY"), Context.RECEIVER_EXPORTED)
        Log.d("MainActivity", "registerReceiver called in onCreate")

        syncCurrentSongFromPrefs()

        // NavController 설정
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        // BottomNavigationView와 NavController 연결
        binding.bottomNavi.setupWithNavController(navController)

        val colorStateList = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf()),
            intArrayOf(
                ContextCompat.getColor(this, R.color.colorSelected),
                ContextCompat.getColor(this, R.color.colorUnselected)
            )
        )
        binding.bottomNavi.itemIconTintList = colorStateList

        // ActivityResultLauncher 등록
        songActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val newTitle = data?.getStringExtra("title") ?: "Unknown"
                val newArtist = data?.getStringExtra("artist") ?: "Unknown"
                val message = "$newTitle - $newArtist"

                val progress = data?.getIntExtra("progress", 0) ?: 0
                val duration = data?.getIntExtra("duration", 100) ?: 100

                binding.mainplayerTitle.text = newTitle
                binding.mainplayerArtist.text = newArtist
                binding.mainplayerSb.max = duration
                binding.mainplayerSb.progress = progress

                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        // 미니 플레이어 클릭 시 SongActivity 실행
        binding.mainplayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("title", binding.mainplayerTitle.text.toString())
                putExtra("artist", binding.mainplayerArtist.text.toString())
            }
            songActivityLauncher.launch(intent)
        }

        // BottomNavigationView 아이템 선택 시 애니메이션 적용
        binding.bottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navigateWithAnimation(R.id.nav_home)
                    true
                }
                R.id.nav_pencil -> {
                    navigateWithAnimation(R.id.nav_pencil)
                    true
                }
                R.id.nav_calendar -> {
                    navigateWithAnimation(R.id.nav_calendar)
                    true
                }
                R.id.nav_profile -> {
                    navigateWithAnimation(R.id.nav_profile)
                    true
                }
                else -> false
            }
        }

        binding.mainPrevBtn.setOnClickListener {
            Log.d("MainActivity", "Previous button clicked")
            moveToPrevSong()
            Log.d("MainActivity", "nowpos : $nowPos")
        }

        binding.mainNextBtn.setOnClickListener {
            Log.d("MainActivity", "Next button clicked")
            moveToNextSong()
            Log.d("MainActivity", "nowpos : $nowPos")
        }
    }

    override fun onResume(){
        super.onResume()
        initSongFromPrefs()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(progressReceiver)
        Log.d("MainActivity", "unregisterReceiver called in onDestroy")
    }

    private fun navigateWithAnimation(destinationId: Int) {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .build()

        val navController: NavController = findNavController(R.id.fragment_container)
        navController.navigate(destinationId, null, navOptions)
    }

    private fun insertDummySong() {
        val db = SongDatabase.getInstance(this) // Singleton Room Database
        val songDao = db.songDao()

        val dummySong = Song(
            title = "Dummy Title",
            singer = "Dummy Artist",
            second = 0,
            playTime = 240000, // 예: 4분을 밀리초 단위로 저장한 경우
            isPlaying = false,
            music = "dummy_music.mp3", // 파일명이나 경로
            coverImg = null, // 혹은 R.drawable.some_image_id
            isLike = false
        )

//        Thread {
////            val insertedId = songDao.insert(dummySong) // 반환값은 Primary Key (Long)
////            saveSongIdToPrefs(insertedId.toInt()) // SharedPreferences에 저장
////            Log.d("MainActivity", "Dummy song inserted with id: $insertedId")
//            initSongFromPrefs() // 초기화
//        }.start()
    }

    private fun saveSongIdToPrefs(songId: Int) {
        val prefs = getSharedPreferences("song_prefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("songId", songId+1).apply()
        Log.d("SharedPrefs", "MainActivity: 저장된 songId = $songId")
    }

    private fun initSongFromPrefs() {
        val prefs = getSharedPreferences("song_prefs", Context.MODE_PRIVATE)
        val songId = prefs.getInt("songId", -1)
        Log.d("SharedPrefs", "MainActivity: 불러온 savedSongId = $songId")
        nowPos = songId - 1
        if (songId == -1) return

        val db = SongDatabase.getInstance(this)
        val songDao = db.songDao()

        Thread {
            val song = songDao.getSongById(songId)
            song?.let {
                runOnUiThread {
                    binding.mainplayerTitle.text = it.title
                    binding.mainplayerArtist.text = it.singer
                    binding.mainplayerSb.max = it.playTime
                    binding.mainplayerSb.progress = it.second
                }
            }
        }.start()
    }

    private fun loadAllSongs() {
        val db = SongDatabase.getInstance(this)
        val songDao = db.songDao()

        Thread {
            songs = songDao.getAllSongs()

            val prefs = getSharedPreferences("song_prefs", Context.MODE_PRIVATE)
            val songId = prefs.getInt("songId", -1)

            // 해당 songId가 리스트에 있는지 확인
            nowPos = songs.indexOfFirst { it.id == songId }
            if (nowPos == -1) {
                nowPos = 0 // 기본값
                if (songs.isNotEmpty()) {
                    saveSongIdToPrefs(songs[0].id)
                }
            }

            runOnUiThread {
                if (songs.isNotEmpty() && nowPos in songs.indices) {
                    updateMiniPlayer(songs[nowPos])
                } else {
                    Log.e("MainActivity", "loadAllSongs: 유효한 곡이 없음 또는 인덱스 오류")
                }
            }
        }.start()
    }

    private fun moveToNextSong() {
        if (songs.isEmpty()) return
        nowPos = (nowPos + 1) % songs.size
        Log.d("MainActivity", "nowpos : $nowPos")
        Log.d("MainActivity", "songs : $songs")
        updateMiniPlayer(songs[nowPos])
    }

    private fun moveToPrevSong() {
        if (songs.isEmpty()) return
        nowPos = if (nowPos - 1 < 0) songs.size - 1 else nowPos - 1

        Log.d("MainActivity", "nowpos : $nowPos")
        Log.d("MainActivity", "songs : $songs")
        updateMiniPlayer(songs[nowPos])
    }

    private fun updateMiniPlayer(song: Song) {
        binding.mainplayerTitle.text = song.title
        binding.mainplayerArtist.text = song.singer
        binding.mainplayerSb.max = song.playTime
        binding.mainplayerSb.progress = song.second
        saveSongIdToPrefs(nowPos)
    }

    private fun syncCurrentSongFromPrefs() {
        val prefs = getSharedPreferences("song_prefs", MODE_PRIVATE)
        val savedSongId = prefs.getInt("songId", -1)
        if (savedSongId == -1) return

        val db = Room.databaseBuilder(
            applicationContext,
            SongDatabase::class.java,
            "song-database"
        ).allowMainThreadQueries().build()

        val songDao = db.songDao()

        val song = songDao.getSongById(savedSongId)
        if (song != null) {
            binding.mainplayerTitle.text = song.title
            binding.mainplayerArtist.text = song.singer
            binding.mainplayerSb.max = song.playTime
            binding.mainplayerSb.progress = 0 // 필요에 따라 현재 재생 위치 동기화 추가 가능
            // 미니 플레이어 재생/일시정지 버튼 상태도 동기화 가능
        }
    }
}
