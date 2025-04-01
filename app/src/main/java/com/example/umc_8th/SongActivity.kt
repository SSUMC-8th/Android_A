package com.example.umc_8th

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import umc.study.umc_8th.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root) // 바인딩을 통한 레이아웃 설정

        // MainActivity에서 전달받은 데이터 가져오기
        val title = intent.getStringExtra("title") ?: "Unknown"
        val artist = intent.getStringExtra("artist") ?: "Unknown"

        // 뷰 바인딩을 통해 UI에 데이터 설정
        binding.songTitleTv.text = title
        binding.songArtistTv.text = artist

        // 아래쪽 화살표 버튼 클릭 시 MainActivity로 데이터 전달 후 종료
        binding.songDownIbtn.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("title", binding.songTitleTv.text.toString()) // 변경된 제목 전달
                putExtra("artist", binding.songArtistTv.text.toString()) // 변경된 가수명 전달
                putExtra("toastMessage", "노래 정보가 업데이트되었습니다!")
            }
            setResult(RESULT_OK, resultIntent)
            finish() // SongActivity 종료
        }
    }
}