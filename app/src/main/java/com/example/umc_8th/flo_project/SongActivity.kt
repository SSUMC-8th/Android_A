package com.example.umc_8th.flo_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var title : String?=null
        var singer : String?=null
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
            title = intent.getStringExtra("title")
            singer =intent.getStringExtra("singer")
            binding.songTitleTv.text=intent.getStringExtra("title")
            binding.songSingerTv.text=intent.getStringExtra("singer")
        }
        binding.songDownIbtn.setOnClickListener{
//            val intent = Intent(this, FloMainActivity::class.java).apply {
//                putExtra("message", title + " " + singer)
//            }
            val resultIntent = Intent().apply {
                putExtra("message", "$title $singer")
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        binding.songPlayerPlayIbtn.setOnClickListener{
            Log.d("SongActivity", "Play button clicked")
            PlayerStatus(true)
        }
        binding.songPlayerPauseIbtn.setOnClickListener{
            Log.d("SongActivity", "Pause button clicked")
            PlayerStatus(false)
        }

    }
    fun PlayerStatus(isPlaying:Boolean){
        if(isPlaying){
            binding.songPlayerPauseIbtn.visibility= View.VISIBLE
            binding.songPlayerPlayIbtn.visibility=View.GONE
        }
        else{
            binding.songPlayerPlayIbtn.visibility=View.VISIBLE
            binding.songPlayerPauseIbtn.visibility=View.GONE
        }
    }
}