package com.example.umc_8th.flo_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.umc_8th.databinding.ActivityMemoBinding
//import umc.study.umc_8th.R
//import umc.study.umc_8th.databinding.ActivityMemoBinding

class MemoActivity : AppCompatActivity() {
    lateinit var binding: ActivityMemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nextBtn.setOnClickListener{
            val intent = Intent(this, MemoJangActivity::class.java)
            val memoText=binding.memoEt.text.toString()
            intent.putExtra("memo", memoText)
            startActivity(intent)
        }
    }
}