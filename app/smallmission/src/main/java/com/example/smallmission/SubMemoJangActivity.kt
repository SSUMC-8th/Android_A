package com.example.smallmission

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smallmission.databinding.ActivitySubMemoJangBinding

class SubMemoJangActivity : AppCompatActivity() {
    lateinit var binding:ActivitySubMemoJangBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivitySubMemoJangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.hasExtra("memo")){
            binding.memoJangText.text=intent.getStringExtra("memo")!!
        }

    }
}