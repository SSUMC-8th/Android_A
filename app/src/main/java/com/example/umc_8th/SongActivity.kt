package com.example.umc_8th

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    lateinit var mediaPlayer: MediaPlayer
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root) // 바인딩을 통한 레이아웃 설정

        // 미디어 초기화
        mediaPlayer = MediaPlayer.create(this, R.raw.lilac)

        // SeekBar 설정
        binding.songPlaySb.max = mediaPlayer.duration
        binding.totalTimeTv.text = formatTime(mediaPlayer.duration)

        // 재생 버튼 클릭 시
        binding.songPlayerPlayIbtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                binding.songPlayerPlayIbtn.setImageResource(R.drawable.btn_miniplayer_play)
            } else {
                mediaPlayer.start()
                binding.songPlayerPlayIbtn.setImageResource(R.drawable.btn_miniplay_pause)
                updateSeekBar()
            }
        }

        // SeekBar 변경 감지
        binding.songPlaySb.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer.seekTo(progress)
                binding.currentTimeTv.text = formatTime(progress)
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

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

    private fun formatTime(ms: Int): String {
        val minutes = ms / 1000 / 60
        val seconds = (ms / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun updateSeekBar() {
        binding.songPlaySb.progress = mediaPlayer.currentPosition
        binding.currentTimeTv.text = formatTime(mediaPlayer.currentPosition)
        if (mediaPlayer.isPlaying) {
            handler.postDelayed({ updateSeekBar() }, 1000)
        }
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()

        handler.removeCallbacksAndMessages(null)
    }
}