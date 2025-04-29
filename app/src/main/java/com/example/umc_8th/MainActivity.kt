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
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var songActivityLauncher: ActivityResultLauncher<Intent>

    private val progressReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val progress = intent?.getIntExtra("currentPosition", 0) ?: 0
            Log.d("ProgressReceiver", "Received progress: $progress")  // 로그 추가
            binding.mainplayerSb.progress = progress
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        registerReceiver(progressReceiver, IntentFilter("com.example.umc_8th.UPDATE_PROGRESS"), RECEIVER_NOT_EXPORTED)
        Log.d("MainActivity", "registerReceiver called")
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(progressReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NavController 설정
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        // BottomNavigationView와 NavController 연결
        binding.bottomNavi.setupWithNavController(navController)

        val colorStateList = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf()),
            intArrayOf(
                ContextCompat.getColor(this, R.color.colorSelected),  // 선택된 아이템 색상 (빨간색)
                ContextCompat.getColor(this, R.color.colorUnselected) // 선택되지 않은 아이템 색상 (회색)
            )
        )
        binding.bottomNavi.itemIconTintList = colorStateList
        binding.bottomNavi.itemIconTintList = colorStateList

        // 최신 방식으로 ActivityResultLauncher 등록
        songActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val newTitle = data?.getStringExtra("title") ?: "Unknown"
                val newArtist = data?.getStringExtra("artist") ?: "Unknown"
                val message = newTitle+"-"+newArtist

                val progress = result.data?.getIntExtra("progress", 0) ?: 0
                val duration = result.data?.getIntExtra("duration", 100) ?: 100

                // 뷰 바인딩을 통해 UI 업데이트
                binding.mainplayerTitle.text = newTitle
                binding.mainplayerArtist.text = newArtist
                binding.mainplayerSb.max = duration
                binding.mainplayerSb.progress = progress
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        // 미니 플레이어 클릭 시 SongActivity로 이동
        binding.mainplayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("title", binding.mainplayerTitle.text.toString())
                putExtra("artist", binding.mainplayerArtist.text.toString())
            }
            songActivityLauncher.launch(intent)
        }

        // BottomNavigationView 아이템 선택 리스너 설정
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

    // Fragment 전환 시 애니메이션을 적용하는 함수
    private fun navigateWithAnimation(destinationId: Int) {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)    // 새 프래그먼트가 들어올 때
            .setExitAnim(R.anim.slide_out_left)     // 기존 프래그먼트가 나갈 때
            .build()

        val navController: NavController = findNavController(R.id.fragment_container)
        navController.navigate(destinationId, null, navOptions)
    }


}