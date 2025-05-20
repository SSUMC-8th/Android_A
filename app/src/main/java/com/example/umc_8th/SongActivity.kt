// SongActivity.kt
package com.example.umc_8th

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
// import androidx.room.Room // Room 임포트 삭제
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivitySongBinding
import androidx.core.content.edit

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private lateinit var mediaPlayer: MediaPlayer
    private val handler = Handler(Looper.getMainLooper())
    private var isRepeatMode = false

    private lateinit var songs: List<Song>
    private var nowPos: Int = 0 // songs 리스트 내에서의 인덱스

    private lateinit var songDao: SongDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Room DB 초기화 및 SongDao 가져오기 (싱글톤 사용)
        val db = SongDatabase.getInstance(applicationContext) // applicationContext 사용
        songDao = db.songDao()

        // 인텐트로부터 songId 가져오기
        val initialSongId = intent.getIntExtra("songId", -1)
        Log.d("SongActivity", "Received initial songId: $initialSongId")

        // 데이터베이스에서 모든 노래를 로드
        songs = songDao.getAllSongs()

        Log.d("SongActivity_DB_Content", "데이터베이스에서 로드된 노래 목록:")
        if (songs.isEmpty()) {
            Log.d("SongActivity_DB_Content", "데이터베이스에 저장된 노래가 없습니다. 홈에서 앨범을 재생해야 합니다.")
            Toast.makeText(this, "재생할 노래가 없습니다. 홈에서 앨범을 선택해주세요.", Toast.LENGTH_LONG).show()
            // 재생 관련 UI 요소 비활성화
            binding.songPlayerPlayIbtn.isEnabled = false
            binding.songPlaySb.isEnabled = false
            binding.songPlayerPrevIbtn.isEnabled = false
            binding.songPlayerNextIbtn.isEnabled = false
        } else {
            songs.forEachIndexed { index, song ->
                Log.d("SongActivity_DB_Content", "  [$index] ID: ${song.id}, Title: \"${song.title}\", Artist: \"${song.singer}\", Music File: \"${song.music}\"")
            }
        }

        // 초기 songId를 사용하여 nowPos 설정
        nowPos = songs.indexOfFirst { it.id == initialSongId }.takeIf { it != -1 } ?: 0
        // 만약 initialSongId가 유효하지 않거나 songs 리스트에 없으면, 첫 번째 곡으로 nowPos를 설정

        // 데이터베이스에 노래가 있는 경우에만 playSong 호출
        if (songs.isNotEmpty()) {
            playSong(nowPos)
        }


        // DB 초기화 버튼 (테스트용)
        binding.resetDbBtn.setOnClickListener {
            resetDatabase()
        }

        // 재생/일시정지 버튼
        binding.songPlayerPlayIbtn.setOnClickListener {
            if (this::mediaPlayer.isInitialized) {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    binding.songPlayerPlayIbtn.setImageResource(R.drawable.btn_miniplayer_play)
                } else {
                    mediaPlayer.start()
                    binding.songPlayerPlayIbtn.setImageResource(R.drawable.btn_miniplay_pause)
                    updateSeekBar()
                }
            } else {
                Toast.makeText(this, "재생할 곡이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // SeekBar 제어
        binding.songPlaySb.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser && this@SongActivity::mediaPlayer.isInitialized) mediaPlayer.seekTo(progress)
                binding.currentTimeTv.text = formatTime(progress)
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        // 반복 버튼
        binding.songRepeatIbtn.setOnClickListener {
            isRepeatMode = !isRepeatMode
            if (isRepeatMode) {
                binding.songRepeatIbtn.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
            } else {
                binding.songRepeatIbtn.colorFilter = null
            }
        }

        // 이전 곡
        binding.songPlayerPrevIbtn.setOnClickListener {
            if (songs.isNotEmpty()) {
                nowPos = (nowPos - 1 + songs.size) % songs.size
                playSong(nowPos)
            }
        }

        // 다음 곡
        binding.songPlayerNextIbtn.setOnClickListener {
            if (songs.isNotEmpty()) {
                nowPos = (nowPos + 1) % songs.size
                playSong(nowPos)
            }
        }

        binding.songDownIbtn.setOnClickListener {
            // 현재 재생 중인 곡의 ID를 SharedPreferences에 저장
            if (songs.isNotEmpty()) {
                saveCurrentSongIdToPrefs(songs[nowPos].id)
            }

            // MainActivity로 결과 반환
            val resultIntent = Intent().apply {
                putExtra("title", binding.songTitleTv.text.toString())
                putExtra("artist", binding.songArtistTv.text.toString())
                putExtra("progress", if (this@SongActivity::mediaPlayer.isInitialized) mediaPlayer.currentPosition else 0)
                putExtra("duration", if (this@SongActivity::mediaPlayer.isInitialized) mediaPlayer.duration else 0)
                putExtra("toastMessage", "노래 정보가 업데이트되었습니다!")
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun playSong(position: Int) {
        if (songs.isEmpty() || position < 0 || position >= songs.size) {
            Log.e("SongActivity", "playSong: 재생할 노래가 없거나 유효하지 않은 인덱스입니다. Position: $position, Songs size: ${songs.size}")
            Toast.makeText(this, "재생할 노래가 없습니다.", Toast.LENGTH_SHORT).show()
            binding.songTitleTv.text = "재생 중 아님"
            binding.songArtistTv.text = "---"
            binding.songPlaySb.progress = 0
            binding.currentTimeTv.text = formatTime(0)
            binding.totalTimeTv.text = formatTime(0)
            binding.songPlayerPlayIbtn.setImageResource(R.drawable.btn_miniplayer_play)
            handler.removeCallbacksAndMessages(null)
            if (this::mediaPlayer.isInitialized) {
                mediaPlayer.stop()
                mediaPlayer.release()
            }
            return
        }

        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
            handler.removeCallbacksAndMessages(null)
        }

        val song = songs[position]
        Log.d("SongActivity", "Playing song: ${song.title} (Music File from DB: ${song.music})")

        val resourceName = song.music.removeSuffix(".mp3")
        val songResId = resources.getIdentifier(
            resourceName,
            "raw",
            packageName
        )

        if (songResId == 0) {
            Log.e("SongActivity", "리소스를 찾을 수 없습니다: raw/$resourceName.mp3. 재생할 수 없습니다.")
            Toast.makeText(this, "${song.title} 음악 파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            binding.songTitleTv.text = "파일 없음"
            binding.songArtistTv.text = "확인 필요"
            binding.songPlayerPlayIbtn.setImageResource(R.drawable.btn_miniplayer_play)
            handler.removeCallbacksAndMessages(null)
            if (this::mediaPlayer.isInitialized) {
                mediaPlayer.stop()
                mediaPlayer.release()
            }
            return
        } else {
            Log.d("SongActivity", "Found resource ID: $songResId for ${song.music}")
            mediaPlayer = MediaPlayer.create(this, songResId)
        }

        mediaPlayer.start()

        binding.songTitleTv.text = song.title
        binding.songArtistTv.text = song.singer
        binding.songPlaySb.max = mediaPlayer.duration
        binding.songPlaySb.progress = 0
        binding.totalTimeTv.text = formatTime(mediaPlayer.duration)
        binding.currentTimeTv.text = formatTime(0)
        binding.songPlayerPlayIbtn.setImageResource(R.drawable.btn_miniplay_pause)

        updateSeekBar()
        saveCurrentSongIdToPrefs(song.id) // 현재 재생 중인 곡의 ID 저장

        mediaPlayer.setOnCompletionListener {
            if (isRepeatMode) {
                playSong(nowPos)
            } else {
                nowPos = (nowPos + 1) % songs.size
                playSong(nowPos)
            }
        }
    }

    private fun updateSeekBar() {
        binding.songPlaySb.progress = mediaPlayer.currentPosition
        binding.currentTimeTv.text = formatTime(mediaPlayer.currentPosition)
        if (mediaPlayer.isPlaying) {
            handler.postDelayed({ updateSeekBar() }, 1000)
        }
    }

    // SharedPreferences에 현재 재생 중인 곡의 ID를 저장하는 함수
    private fun saveCurrentSongIdToPrefs(songId: Int) {
        getSharedPreferences("song_prefs", MODE_PRIVATE).edit() {
            putInt("songId", songId)
        }
        Log.d("SharedPrefs", "SongActivity: 저장된 songId = $songId")
    }

    private fun formatTime(ms: Int): String {
        val minutes = ms / 1000 / 60
        val seconds = (ms / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun resetDatabase() {
        // Room 데이터베이스 인스턴스 파괴 (싱글톤)
        SongDatabase.destroyInstance()
        // 새로운 데이터베이스 인스턴스 가져오기 (초기화)
        val db = SongDatabase.getInstance(applicationContext)
        songDao = db.songDao()

        songDao.deleteAll()
        songs = emptyList()

        binding.songTitleTv.text = "재생 중 아님"
        binding.songArtistTv.text = "---"
        binding.songPlaySb.progress = 0
        binding.currentTimeTv.text = formatTime(0)
        binding.totalTimeTv.text = formatTime(0)
        binding.songPlayerPlayIbtn.setImageResource(R.drawable.btn_miniplayer_play)
        handler.removeCallbacksAndMessages(null)
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }

        Log.d("SongActivity_DB_Content", "DB 초기화 후 로드된 노래 목록 (비어있음):")
        Toast.makeText(this, "DB가 초기화되었습니다. 홈에서 앨범을 다시 선택해주세요.", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}