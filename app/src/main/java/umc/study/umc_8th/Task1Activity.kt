package umc.study.umc_8th

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Task1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task1)

        val btn_smile = findViewById<ImageButton>(R.id.btn_smile)
        val btn_bigsmile = findViewById<ImageButton>(R.id.btn_bigsmile)
        val btn_nonsmile = findViewById<ImageButton>(R.id.btn_nonsmile)
        val btn_sad = findViewById<ImageButton>(R.id.btn_sad)
        val btn_angry = findViewById<ImageButton>(R.id.btn_angry)


        btn_smile.setOnClickListener {
            val intent = Intent(this, Taskone_temp1::class.java)
            intent.putExtra("text", "smile")
            startActivity(intent)
        }

        btn_bigsmile.setOnClickListener {
            val intent = Intent(this, Taskone_temp1::class.java)
            intent.putExtra("text", "bigsmile")
            startActivity(intent)
        }

        btn_nonsmile.setOnClickListener {
            val intent = Intent(this, Taskone_temp1::class.java)
            intent.putExtra("text", "nonsmile")
            startActivity(intent)
        }

        btn_sad.setOnClickListener {
            val intent = Intent(this, Taskone_temp1::class.java)
            intent.putExtra("text", "sad")
            startActivity(intent)
        }

        btn_angry.setOnClickListener {
            val intent = Intent(this, Taskone_temp1::class.java)
            intent.putExtra("text", "angry")
            startActivity(intent)
        }







        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
}