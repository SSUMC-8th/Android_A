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
import androidx.room.Room
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivitySongBinding
import androidx.core.content.edit

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private lateinit var mediaPlayer: MediaPlayer
    private val handler = Handler(Looper.getMainLooper())
    private var isRepeatMode = false

    private lateinit var songs: List<Song>
    private var nowPos: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val db = Room.databaseBuilder(
            applicationContext,
            SongDatabase::class.java,
            "song-database"
        ).allowMainThreadQueries().build()

        val songDao = db.songDao()
        val dummySongs = listOf(
            Song(title = "LILAC", singer = "IU", playTime = 210000, music = "lilac.mp3"),
            Song(title = "Coin", singer = "IU", playTime = 200000, music = "coin.mp3"),
            Song(title = "HiSpringBye", singer = "IU", playTime = 230000, music = "hi_spring_bye.mp3"),
            Song(title = "Flu", singer = "IU", playTime = 180000, music = "flu.mp3")
        )
        // 첫 실행 시 더미 데이터 삽입
        if (isFirstRun()) {
            dummySongs.forEach { songDao.insert(it) }
        }

        songs = songDao.getAllSongs()

        //불러올때
        val savedSongId = getSharedPreferences("song_prefs", MODE_PRIVATE).getInt("songId", -1)
        Log.d("SongActivity", "Saved songId from prefs: $savedSongId")
        nowPos = songs.indexOfFirst { it.id == savedSongId }.takeIf { it != -1 } ?: 0


        playSong(nowPos)


        binding.resetDbBtn.setOnClickListener {
            resetDatabase(songDao)
        }

        // 재생/일시정지 버튼
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

        // SeekBar 제어
        binding.songPlaySb.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer.seekTo(progress)
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
            if (nowPos > 0) {
                nowPos--
                playSong(nowPos)
            }
        }

        // 다음 곡
        binding.songPlayerNextIbtn.setOnClickListener {
            if (nowPos < songs.size - 1) {
                nowPos++
                playSong(nowPos)
            }
        }
        Log.d("SongCheck", songs.joinToString("\n") { "${it.id}: ${it.title} / ${it.music}" })
        // 아래로 내리기 버튼
        binding.songDownIbtn.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("title", binding.songTitleTv.text.toString())
                putExtra("artist", binding.songArtistTv.text.toString())
                putExtra("toastMessage", "노래 정보가 업데이트되었습니다!")
                putExtra("progress", mediaPlayer.currentPosition)
                putExtra("duration", mediaPlayer.duration)
            }
            setResult(RESULT_OK, resultIntent)
            saveCurrentSongId()
            finish()
        }

    }

    private fun playSong(position: Int) {
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
            handler.removeCallbacksAndMessages(null)
        }

        val song = songs[position]
        Log.d("SongActivity", "Playing song: $song")
        val songResId = when (song.music) {
            "lilac.mp3" -> R.raw.lilac
            "coin.mp3" -> R.raw.coin
            "hi_spring_bye.mp3" -> R.raw.hi_spring_bye
            "flu.mp3" -> R.raw.flu
            else -> R.raw.lilac // 기본값
        }
        Log.d("SongCheck", songs.joinToString("\n") { "${it.id}: ${it.title} / ${it.music}" })
        mediaPlayer = MediaPlayer.create(this, songResId)
        mediaPlayer.start()

        binding.songTitleTv.text = song.title
        binding.songArtistTv.text = song.singer
        binding.songPlaySb.max = mediaPlayer.duration
        binding.songPlaySb.progress = 0
        binding.totalTimeTv.text = formatTime(mediaPlayer.duration)
        binding.currentTimeTv.text = formatTime(0)
        binding.songPlayerPlayIbtn.setImageResource(R.drawable.btn_miniplay_pause)

        updateSeekBar()
        saveCurrentSongId()

        mediaPlayer.setOnCompletionListener {
            if (isRepeatMode) {
                playSong(nowPos)
            } else {
                binding.songPlayerPlayIbtn.setImageResource(R.drawable.btn_miniplayer_play)
                handler.removeCallbacksAndMessages(null)
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

    private fun saveCurrentSongId() {
        val songId = songs[nowPos].id
        getSharedPreferences("song_prefs", MODE_PRIVATE).edit() {
            putInt("songId", songId)
        }
        Log.d("SharedPrefs", "SongActivity: 저장된 songId = $songId")
    }

    private fun isFirstRun(): Boolean {
        val prefs = getSharedPreferences("song_init", MODE_PRIVATE)  // 분리된 prefs 이름 추천
        val isFirst = prefs.getBoolean("isFirst", true)
        Log.d("isFirst", "$isFirst")
        if (isFirst) {
            prefs.edit().putBoolean("isFirst", false).apply()
        }
        return isFirst
    }


    private fun formatTime(ms: Int): String {
        val minutes = ms / 1000 / 60
        val seconds = (ms / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun resetDatabase(songDao: SongDao) {
        // 1. 기존 데이터 삭제
        songDao.deleteAll()

        // 2. 더미 데이터 다시 삽입
        val dummySongs = listOf(
            Song(title = "LILAC", singer = "IU", playTime = 210000, music = "lilac.mp3"),
            Song(title = "Coin", singer = "IU", playTime = 200000, music = "coin.mp3"),
            Song(title = "HiSpringBye", singer = "IU", playTime = 230000, music = "hi_spring_bye.mp3"),
            Song(title = "Flu", singer = "IU", playTime = 180000, music = "flu.mp3")
        )
        dummySongs.forEach { songDao.insert(it) }

        // 3. 리스트 재갱신
        songs = songDao.getAllSongs()
        songDao.resetAutoIncrement()

        // 4. 초기 위치로 재생
        nowPos = 0
        playSong(nowPos)

        Toast.makeText(this, "DB가 초기화되었습니다.", Toast.LENGTH_SHORT).show()
    }



    override fun onDestroy() {
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
