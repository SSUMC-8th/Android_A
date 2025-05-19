package com.example.umc_8th

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_8th.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var mbinding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(mbinding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}