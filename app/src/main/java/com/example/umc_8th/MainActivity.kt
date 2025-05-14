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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var songActivityLauncher: ActivityResultLauncher<Intent>

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

        // BroadcastReceiver 등록 (onCreate에서 항상 등록)
        registerReceiver(albumPlayReceiver, IntentFilter("com.example.umc_8th.ALBUM_PLAY"), Context.RECEIVER_EXPORTED)
        Log.d("MainActivity", "registerReceiver called in onCreate")

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
}
