package com.example.smallmission

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smallmission.databinding.ActivitySubMemoBinding

class SubMemoActivity : AppCompatActivity() {
    lateinit var binding:ActivitySubMemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivitySubMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nextBtn.setOnClickListener{
            val intent = Intent(this, SubMemoJangActivity::class.java)
            val memoText=binding.memoEt.text.toString()
            intent.putExtra("memo", memoText)
            startActivity(intent)
        }
    }
}