package com.example.floclone

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SongActivity : AppCompatActivity() {

    private lateinit var arrowDownButton : ImageButton
    private var returnString: String = "제목"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_song)

        //전달 데이터
        val title = intent.getStringExtra("title") ?: "제목"
        val artist = intent.getStringExtra("artist") ?: "가수"
        val lyric1 = intent.getStringExtra("lyric1") ?: "가사1"
        val lyric2 = intent.getStringExtra("lyric2") ?: "가사2"

        findViewById<TextView>(R.id.tv_songName_songActivity).text = title
        findViewById<TextView>(R.id.tv_artistName_songActivity).text = artist
        findViewById<TextView>(R.id.tv_lyric1_songActivity).text = lyric1
        findViewById<TextView>(R.id.tv_lyric2_songActivity).text = lyric2

        returnString = title

        arrowDownButton = findViewById(R.id.btn_arrowDown_songActivity)
        arrowDownButton.setOnClickListener({
            returnHome()
        })


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun returnHome(){
        val resultIntent = Intent()
        resultIntent.putExtra("title", returnString)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}