package com.example.floclone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private lateinit var clHomeplayer : ConstraintLayout

    //registerForActivityResult
    private val songResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val returnString = result.data?.getStringExtra("title")
            Toast.makeText(this, "Title: $returnString", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        clHomeplayer = findViewById(R.id.cl_homeplayer)
        clHomeplayer.setOnClickListener({
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title","Lady")
            intent.putExtra("artist", "Kenshi Yonezu")
            intent.putExtra("lyric1", "例えば僕ら二人 煌めく映画のように")
            intent.putExtra("lyric2", "出会いなおせたらどうしたい")

            //intent를 만들고 startActivity 대신 registerForActivityResult를 수행
            songResultLauncher.launch(intent)
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }
}