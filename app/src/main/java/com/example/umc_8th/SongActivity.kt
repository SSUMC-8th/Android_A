package com.example.umc_8th

import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_8th.databinding.ActivitySongBinding



class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private val songViewModel: SongViewModel by viewModels() // ViewModel을 사용합니다.
    private var currentSongId: Int = -1

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

        currentSongId = intent.getIntExtra("songId", -1)
        val title = intent.getStringExtra("songTitle")
        val artist = intent.getStringExtra("songArtist")

        // 받은 데이터를 UI에 반영
        binding.songTitle.text = title ?: "제목 없음"
        binding.songArtist.text = artist ?: "아티스트 없음"

        // 좋아요 상태를 LiveData로 관찰
        songViewModel.isLiked.observe(this) { isLiked ->
            updateLikeButton(isLiked)
        }

        // 플레이 상태 리스너
        MusicPlayerState.addPlayListener(playStateListener)
        binding.songPlayBtn.setOnClickListener {
            MusicPlayerState.togglePlay()
        }

        // 진행 상태 리스너
        MusicPlayerState.addProgressListener(progressListener)

        // SeekBar thumb 제거 및 사용자 이벤트 처리
        binding.timeSeekBar.thumb = null
        binding.timeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayerState.setProgress(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 좋아요 버튼 클릭 리스너
        binding.likeBtn.setOnClickListener {
            songViewModel.toggleLikeStatus(currentSongId)
        }

        // 좋아요 상태 로딩
        songViewModel.loadSongData(currentSongId)
    }

    private fun updateLikeButton(isLiked: Boolean) {
        binding.likeBtn.setImageResource(
            if (isLiked) R.drawable.ic_my_like_on
            else R.drawable.ic_my_like_off
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicPlayerState.removePlayListener(playStateListener)
        MusicPlayerState.removeProgressListener(progressListener)
    }
}


//
//class SongActivity :AppCompatActivity(){
//
//    private lateinit var binding: ActivitySongBinding
//
//    private val playStateListener: (Boolean) -> Unit = { isPlaying ->
//        binding.songPlayBtn.setImageResource(
//            if (isPlaying) R.drawable.btn_miniplay_pause
//            else R.drawable.btn_miniplayer_play
//        )
//    }
//
//    private val progressListener: (Int) -> Unit = { progress ->
//        binding.timeSeekBar.progress = progress
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//        binding = ActivitySongBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
//        val title = intent.getStringExtra("songTitle") //putExtra에서 보낸 name과 같아야함
//        val artist = intent.getStringExtra("songArtist")
//
//        // 받아온 데이터를 뷰에 반영
//        binding.songTitle.text = title ?: "제목 없음"
//        binding.songArtist.text = artist ?: "아티스트 없음"
//
//
//        //재생 상태 리스너(play/pause)
//        MusicPlayerState.addPlayListener(playStateListener)
//        binding.songPlayBtn.setOnClickListener {
//            MusicPlayerState.togglePlay()
//        }
//
//        //진행도 리스너
//        MusicPlayerState.addProgressListener(progressListener)
//
//        MusicPlayerState.addProgressListener { progress ->
//            binding.timeSeekBar.progress = progress
//        }
//
//        binding.timeSeekBar.thumb = null
//
//
//
//        //사용자 클릭에 반응
//        binding.timeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                if (fromUser) {
//                    MusicPlayerState.setProgress(progress)
//                }
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                // 필요하다면 일시정지 시킬 수도 있음
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                // 드래그가 끝났을 때, 자동 진행은 그대로 유지됨
//            }
//        })
//
//
//
//    }
//    override fun onDestroy() {
//        super.onDestroy()
//        MusicPlayerState.removePlayListener(playStateListener)
//        MusicPlayerState.removeProgressListener(progressListener)
//    }
//}
