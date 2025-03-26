package com.example.umc_8th

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import umc.study.umc_8th.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // splash 화면에 보여줄 레이아웃 설정

        lifecycleScope.launch {
            delay(3000) // 3초 대기
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish() // 현재 액티비티 종료
        }
    }
}