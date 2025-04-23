package com.example.umc_8th

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.umc_8th.databinding.ActivitySongBinding
import com.example.umc_8th.MusicPlayerState


class SongActivity :AppCompatActivity(){

    private lateinit var binding: ActivitySongBinding

    private val playStateListener: (Boolean) -> Unit = { isPlaying ->
        binding.songPlayBtn.setImageResource(
            if (isPlaying) R.drawable.btn_miniplay_pause
            else R.drawable.btn_miniplayer_play
        )
    }

    private val progressListener: (Int) -> Unit = { progress ->
        binding.timeSeekBar.progress = progress
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val title = intent.getStringExtra("songTitle") //putExtra에서 보낸 name과 같아야함
        val artist = intent.getStringExtra("songArtist")

        // 받아온 데이터를 뷰에 반영
        binding.songTitle.text = title ?: "제목 없음"
        binding.songArtist.text = artist ?: "아티스트 없음"


        //재생 상태 리스너(play/pause)
        MusicPlayerState.addPlayListener(playStateListener)
        binding.songPlayBtn.setOnClickListener {
            MusicPlayerState.togglePlay()
        }

        //진행도 리스너
        MusicPlayerState.addProgressListener(progressListener)

        MusicPlayerState.addProgressListener { progress ->
            binding.timeSeekBar.progress = progress
        }

        binding.timeSeekBar.thumb = null



        //사용자 클릭에 반응
        binding.timeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayerState.setProgress(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 필요하다면 일시정지 시킬 수도 있음
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 드래그가 끝났을 때, 자동 진행은 그대로 유지됨
            }
        })



    }
    override fun onDestroy() {
        super.onDestroy()
        MusicPlayerState.removePlayListener(playStateListener)
        MusicPlayerState.removeProgressListener(progressListener)
    }
}
