// MainActivity.kt
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
import java.util.Collections.emptyList

// import androidx.room.Room // Room 임포트 삭제

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var songActivityLauncher: ActivityResultLauncher<Intent>

    private var songs: List<Song> = emptyList()
    private var nowPos: Int = 0

    private lateinit var songDao: SongDao

    private val albumPlayReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.example.umc_8th.ALBUM_PLAY") {
                val title = intent.getStringExtra("title") ?: "Unknown"
                val artist = intent.getStringExtra("artist") ?: "Unknown"
                val progress = intent.getIntExtra("progress", 0)
                val duration = intent.getIntExtra("duration", 100)

                binding.mainplayerTitle.text = title
                binding.mainplayerArtist.text = artist
                binding.mainplayerSb.max = duration
                binding.mainplayerSb.progress = progress

                loadSongsAndSetCurrentPos()

                Toast.makeText(context, "앨범 재생 요청: $title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Room DB 초기화 및 SongDao 가져오기 (싱글톤 사용)
        val db = SongDatabase.getInstance(applicationContext) // applicationContext 사용
        songDao = db.songDao()

        loadSongsAndSetCurrentPos()

        registerReceiver(albumPlayReceiver, IntentFilter("com.example.umc_8th.ALBUM_PLAY"), Context.RECEIVER_EXPORTED)
        Log.d("MainActivity", "AlbumPlayReceiver registered in onCreate")

        songActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                loadSongsAndSetCurrentPos()
            }
        }

        binding.mainplayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                if (songs.isNotEmpty()) {
                    putExtra("songId", songs[nowPos].id)
                } else {
                    putExtra("songId", -1)
                }
            }
            songActivityLauncher.launch(intent)
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavi.setupWithNavController(navController)

        val colorStateList = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf()),
            intArrayOf(
                ContextCompat.getColor(this, R.color.colorSelected),
                ContextCompat.getColor(this, R.color.colorUnselected)
            )
        )
        binding.bottomNavi.itemIconTintList = colorStateList

        binding.bottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { navigateWithAnimation(R.id.nav_home); true }
                R.id.nav_pencil -> { navigateWithAnimation(R.id.nav_pencil); true }
                R.id.nav_calendar -> { navigateWithAnimation(R.id.nav_calendar); true }
                R.id.nav_profile -> { navigateWithAnimation(R.id.nav_profile); true }
                else -> false
            }
        }

        binding.mainPrevBtn.setOnClickListener {
            Log.d("MainActivity", "Previous button clicked")
            moveToPrevSong()
        }

        binding.mainNextBtn.setOnClickListener {
            Log.d("MainActivity", "Next button clicked")
            moveToNextSong()
        }

//        binding.mainPlayBtn.setOnClickListener {
//            if (songs.isNotEmpty()) {
//                val intent = Intent(this, SongActivity::class.java).apply {
//                    putExtra("songId", songs[nowPos].id)
//                }
//                songActivityLauncher.launch(intent)
//            } else {
//                Toast.makeText(this, "재생할 노래가 없습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        loadSongsAndSetCurrentPos()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(albumPlayReceiver)
        Log.d("MainActivity", "AlbumPlayReceiver unregistered in onDestroy")
    }

    private fun navigateWithAnimation(destinationId: Int) {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .build()
        val navController: NavController = findNavController(R.id.fragment_container)
        navController.navigate(destinationId, null, navOptions)
    }

    private fun saveCurrentSongIdToPrefs(songId: Int) {
        val prefs = getSharedPreferences("song_prefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("songId", songId).apply()
        Log.d("SharedPrefs", "MainActivity: 저장된 songId = $songId")
    }

    private fun loadSongsAndSetCurrentPos() {
        Thread {
            songs = songDao.getAllSongs()
            Log.d("MainActivity_DB_Loaded", "MainActivity에 로드된 노래 목록 (${songs.size}개):")
            if (songs.isEmpty()) {
                Log.d("MainActivity_DB_Loaded", "비어있습니다.")
                runOnUiThread {
                    updateMiniPlayerUI(null)
                }
                return@Thread
            } else {
                songs.forEachIndexed { index, song ->
                    Log.d("MainActivity_DB_Loaded", "  [$index] ID: ${song.id}, Title: \"${song.title}\"")
                }
            }

            val prefs = getSharedPreferences("song_prefs", Context.MODE_PRIVATE)
            val savedSongId = prefs.getInt("songId", -1)
            Log.d("SharedPrefs", "MainActivity: 불러온 savedSongId = $savedSongId")

            nowPos = songs.indexOfFirst { it.id == savedSongId }

            if (nowPos == -1) {
                nowPos = 0
                Log.w("MainActivity", "savedSongId($savedSongId)에 해당하는 곡을 찾지 못함. 첫 번째 곡으로 nowPos 설정.")
            }
            if (nowPos < 0 || nowPos >= songs.size) {
                nowPos = 0
                Log.e("MainActivity", "nowPos($nowPos)가 유효 범위를 벗어남. 0으로 강제 설정.")
            }

            runOnUiThread {
                updateMiniPlayerUI(songs[nowPos])
            }
        }.start()
    }

    private fun moveToNextSong() {
        if (songs.isEmpty()) {
            Toast.makeText(this, "재생할 노래가 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos = (nowPos + 1) % songs.size
        Log.d("MainActivity", "moveToNextSong: nowPos = $nowPos, songs.size = ${songs.size}")
        updateMiniPlayerUI(songs[nowPos])
        saveCurrentSongIdToPrefs(songs[nowPos].id)
    }

    private fun moveToPrevSong() {
        if (songs.isEmpty()) {
            Toast.makeText(this, "재생할 노래가 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos = (nowPos - 1 + songs.size) % songs.size // 음수 방지 및 순환
        Log.d("MainActivity", "moveToPrevSong: nowPos = $nowPos, songs.size = ${songs.size}")
        updateMiniPlayerUI(songs[nowPos])
        saveCurrentSongIdToPrefs(songs[nowPos].id)
    }

    private fun updateMiniPlayerUI(song: Song?) {
        if (song != null) {
            binding.mainplayerTitle.text = song.title
            binding.mainplayerArtist.text = song.singer
            binding.mainplayerSb.max = song.playTime
            binding.mainplayerSb.progress = song.second
            binding.mainplayerCl.visibility = View.VISIBLE
            binding.mainplayerSb.visibility = View.VISIBLE
            //binding.mainPlayBtn.setImageResource(R.drawable.btn_miniplayer_play)
        } else {
            binding.mainplayerTitle.text = "재생 중 아님"
            binding.mainplayerArtist.text = ""
            binding.mainplayerSb.progress = 0
            binding.mainplayerSb.max = 0
            binding.mainplayerCl.visibility = View.INVISIBLE
            binding.mainplayerSb.visibility = View.INVISIBLE
            //binding.mainPlayBtn.setImageResource(R.drawable.btn_miniplayer_play)
        }
    }
}