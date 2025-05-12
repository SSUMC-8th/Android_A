package com.example.floclone

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private var databaseReference : DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val tvcheck = findViewById<TextView>(R.id.tv_logincheck);





        /*
        val db = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("like")
        databaseReference!!.push().setValue(likeData).addOnSuccessListener {
            Log.d("Firebase", "저장 성공")
            tvcheck.text = "성공"
        }
            .addOnFailureListener { error ->
                Log.e("Firebase", "저장 실패: ${error.message}")
                tvcheck.text = "실패: ${error.message}"
            }

         */


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}