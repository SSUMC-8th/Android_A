package com.example.umc_8th

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import umc.study.umc_8th.R

class TaskOneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_one)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnVeryHappy = findViewById<ImageButton>(R.id.btn_veryhappy)
        val btnHappy = findViewById<ImageButton>(R.id.btn_happy)
        val btnBoard = findViewById<ImageButton>(R.id.btn_board)
        val btnWuul = findViewById<ImageButton>(R.id.btn_wuul)
        val btnAngry = findViewById<ImageButton>(R.id.btn_angry)

        btnVeryHappy.setOnClickListener { sendIntent("Happy!") }
        btnHappy.setOnClickListener { sendLink("https://www.youtube.com/shorts/7fTHD07Q9Pw?feature=share") }
        btnBoard.setOnClickListener { sendIntent("Board...") }
        btnWuul.setOnClickListener { sendIntent("Sad...") }
        btnAngry.setOnClickListener { sendIntent("Angry!") }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun sendLink(text:String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(text))
        startActivity(intent)
    }

    fun sendIntent(text:String){
        val intent = Intent(this, TaskOneTempActivity::class.java)
        intent.putExtra("sendTemp", text)
        startActivity(intent)
    }

}