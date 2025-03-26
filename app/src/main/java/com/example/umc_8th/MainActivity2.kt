package com.example.umc_8th

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivityMain2Binding


class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.happy.setOnClickListener{
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }
        binding.excited.setOnClickListener{
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }
        binding.normal.setOnClickListener{
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }
        binding.anxiety.setOnClickListener{
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }
        binding.anger.setOnClickListener{
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }

    }
}