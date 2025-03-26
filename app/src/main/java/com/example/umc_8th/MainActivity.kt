package com.example.umc_8th

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import android.content.Intent


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val buttonIds = listOf(
        R.id.happy_button to HappyActivity::class.java,
        R.id.excited_button to ExcitedActivity::class.java,
        R.id.anxious_button to AnxiousActivity::class.java,
        R.id.normal_button to NormalActivity::class.java,
        R.id.angry_button to AngryActivity::class.java,
        R.id.vector_button to MainActivity::class.java
        )
        for ((id, activity) in buttonIds) {
            findViewById<ImageButton>(id).setOnClickListener {
                startActivity(Intent(this, activity))
            }
        }
    }
}