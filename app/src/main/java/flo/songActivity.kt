package com.example.flo

import com.example.flo.databinding.ActivitySongBinding
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class songActivity : AppCompatActivity(){

    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.songDownbuttonIcon.setOnClickListener {
            finish()
        }
        binding.songPlayIcon.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songPauseIcon.setOnClickListener {
            setPlayerStatus(true)
        }
    }
    fun setPlayerStatus(isPlaying : Boolean){
        if(isPlaying){
            binding.songPlayIcon.visibility = View.VISIBLE
            binding.songPauseIcon.visibility = View.GONE
        }else{
            binding.songPlayIcon.visibility = View.GONE
            binding.songPauseIcon.visibility = View.VISIBLE
        }
    }
}