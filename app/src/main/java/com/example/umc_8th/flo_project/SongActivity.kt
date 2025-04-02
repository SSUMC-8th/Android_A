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
    lateinit var song :Song
    lateinit var timer : Timer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

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

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    //프로그래스 바 관련 추가한 부분
    private fun initSong(){
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false)
            )
        }
        startTimer()
    }

    private fun setPlayer(song:Song){
        binding.songTitleTv.text=intent.getStringExtra("title")
        binding.songSingerTv.text=intent.getStringExtra("singer")
        binding.songTimeStartTv.text=String.format("%02d: %02d", song.second / 60, song.second % 60)
        binding.songTimeEndTv.text=String.format("%02d: %02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressV.progress = (song.second * 1000 / song.playTime)

        PlayerStatus(song.isPlaying)
    }

    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }
    private fun PlayerStatus(isPlaying:Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying
        if(isPlaying){
            binding.songPlayerPauseIbtn.visibility= View.VISIBLE
            binding.songPlayerPlayIbtn.visibility=View.GONE
        }
        else{
            binding.songPlayerPlayIbtn.visibility=View.VISIBLE
            binding.songPlayerPauseIbtn.visibility=View.GONE
        }
    }

    inner class Timer(private val playTime : Int, var isPlaying: Boolean = true):Thread(){
        private var second : Int = 0
        private var mills : Float = 0f
        override fun run() {
            super.run()
            try {
                while (true){
                    if(second >= playTime)break
                    if(isPlaying){
                        sleep(50)
                        mills+=50

                        runOnUiThread {
                            binding.songProgressV.progress = ((mills / playTime)*100).toInt()
                        }
                        if(mills % 1000 ==0f){
                            runOnUiThread {
                                binding.songTimeStartTv.text=String.format("%02d: %02d", second / 60, second % 60)

                            }
                            second++
                        }

                    }
                }
            }catch (e:InterruptedException){
                Log.d("Song", "쓰레드 사망 ${e.message}")
            }


        }
    }
}