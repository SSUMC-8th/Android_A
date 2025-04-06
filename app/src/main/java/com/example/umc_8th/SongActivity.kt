package com.example.umc_8th

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.umc_8th.databinding.ActivitySongBinding

class SongActivity :AppCompatActivity(){

    private lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val title = intent.getStringExtra("songTitle") //putExtra에서 보낸 name과 같아야함
        val artist = intent.getStringExtra("songArtist")

        // 받아온 데이터를 뷰에 반영
        binding.songTitle.text = title ?: "제목 없음"
        binding.songArtist.text = artist ?: "아티스트 없음"

        binding.songPlayBtn.setOnClickListener {
            // 현재 버튼 이미지가 play일 경우 pause로 변경
            if (binding.songPlayBtn.drawable.constantState == ContextCompat.getDrawable(this, R.drawable.btn_miniplayer_play)?.constantState) {
                binding.songPlayBtn.setImageResource(R.drawable.btn_miniplay_pause)
            } else {
                binding.songPlayBtn.setImageResource(R.drawable.btn_miniplayer_play)
            }
        }

    }
}
