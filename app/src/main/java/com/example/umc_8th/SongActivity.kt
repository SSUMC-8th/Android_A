package com.example.umc_8th

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    lateinit var mediaPlayer: MediaPlayer
    private val handler = Handler(Looper.getMainLooper())
    private var isRepeatMode = false

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
            if (!this::mediaPlayer.isInitialized || !mediaPlayer.isPlaying) {
                startOneSongPlayback()
            } else {
                mediaPlayer.pause()
                binding.songPlayerPlayIbtn.setImageResource(R.drawable.btn_miniplayer_play)
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

        binding.songRepeatIbtn.setOnClickListener {
            isRepeatMode = !isRepeatMode

            if (isRepeatMode) {
                binding.songRepeatIbtn.setColorFilter(ContextCompat.getColor(this, R.color.purple_500)) // 활성화 아이콘
            } else {
                binding.songRepeatIbtn.colorFilter = null // 비활성화 아이콘
            }
        }

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
                putExtra("progress", mediaPlayer.currentPosition)
                putExtra("duration", mediaPlayer.duration)
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
            broadcastProgress()
            Log.d("SongActivity", "broadcast sent : ${mediaPlayer.currentPosition}")
            handler.postDelayed({ updateSeekBar() }, 1000)
        }
    }

    private fun startOneSongPlayback() {
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
            handler.removeCallbacksAndMessages(null)
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.lilac)

        binding.songPlaySb.max = mediaPlayer.duration
        binding.songPlaySb.progress = 0
        binding.totalTimeTv.text = formatTime(mediaPlayer.duration)
        binding.currentTimeTv.text = formatTime(0)

        mediaPlayer.start()
        binding.songPlayerPlayIbtn.setImageResource(R.drawable.btn_miniplay_pause)

        updateSeekBar()

        mediaPlayer.setOnCompletionListener {
            if (isRepeatMode) {
                startOneSongPlayback() // 한곡 반복
            } else {
                binding.songPlayerPlayIbtn.setImageResource(R.drawable.btn_miniplayer_play)
                handler.removeCallbacksAndMessages(null)
            }
        }
    }

    private fun broadcastProgress() {
        val intent = Intent("com.example.umc_8th.UPDATE_PROGRESS")
        Log.d("SongActivity", "Broadcasting progress: $mediaPlayer.currentPosition")
        intent.putExtra("currentPosition", mediaPlayer.currentPosition)
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()

        handler.removeCallbacksAndMessages(null)
    }
}