package com.example.umc_8th.flo_project

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivityMemoJangBinding

class MemoJangActivity : AppCompatActivity() {
    lateinit var binding:ActivityMemoJangBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding=ActivityMemoJangBinding.inflate(layoutInflater)
        if(intent.hasExtra("memo")){
            binding.memoJangText.text=intent.getStringExtra("memo")!!
        }

    }
}