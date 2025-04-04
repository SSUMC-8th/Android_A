package com.example.smallmission

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smallmission.databinding.ActivitySmallMainBinding

class SmallMainActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySmallMainBinding

    private var isRunning = false
    private var startTime = 0L
    private var pauseTime = 0L
    private var seconds = 0
    private var thread : Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivitySmallMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startPauseBtn.setOnClickListener {
            if(isRunning) pauseTimer()
            else startTimer()
        }

        binding.resetBtn.setOnClickListener {
            resetTimer()
        }

    }

    private fun startTimer(){
        isRunning=true
        binding.startPauseBtn.text="Pause"
        startTime = System.currentTimeMillis() - pauseTime

        thread = Thread {
            try {
                while (isRunning) {
                    val elapsed=System.currentTimeMillis() - startTime
                    runOnUiThread {
                        val hrs = (elapsed / (1000 * 60 * 60)) % 24
                        val mins = (elapsed / (1000 * 60)) % 60
                        val secs = (elapsed / 1000) % 60
                        val millis = elapsed % 1000
                        binding.timeMinTv.text = String.format("%02d:%02d:%02d.%03d", hrs, mins, secs, millis)
                    }
                    Thread.sleep(10)
                    seconds++
                }
            } catch (e: InterruptedException) { }
        }
        thread?.start()
    }

    private fun pauseTimer(){
        isRunning = false
        binding.startPauseBtn.text="Start"
        thread?.interrupt()
    }

    private fun resetTimer(){
        isRunning = false
        thread?.interrupt()
        thread=null

        pauseTime = 0L
        startTime = 0L

        runOnUiThread {
            binding.timeMinTv.text = "00:00:00.000"
            binding.startPauseBtn.text = "Start"
        }
    }
}