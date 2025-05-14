package com.example.umc_8th

import MusicPlayerState
import MusicPlayerState.currentSongId
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.umc_8th.database.SongDatabase
import com.example.umc_8th.databinding.ActivityMain2ndBinding
import com.example.umc_8th.entity.AlbumEntity
import com.example.umc_8th.entity.SongEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity_2nd : AppCompatActivity() {

    private lateinit var mBinding: ActivityMain2ndBinding

    private val playStateListener: (Boolean) -> Unit = { isPlaying ->
        // UI 업데이트는 반드시 메인 스레드에서
        runOnUiThread {
            mBinding.miniPlayBtn.setImageResource(
                if (isPlaying) R.drawable.btn_miniplay_pause
                else R.drawable.btn_miniplayer_play
            )
        }
    }


    private val progressListener: (Int) -> Unit = { progress ->
        mBinding.timeSeekBar.progress = progress
    }

    private fun insertDummyData() {
        val db = SongDatabase.getDatabase(this)
        val albumDao = db.albumDao()
        val songDao = db.songDao()

        CoroutineScope(Dispatchers.IO).launch {
            val existing = albumDao.getAlbums()
            if (existing.isEmpty()) {
                val albumList = listOf(
                    AlbumEntity(albumId = 1, title = "Weekend", singer = "태연", coverImg = R.drawable.img_album_exp6),
                    AlbumEntity(albumId = 2, title = "Lilac", singer = "아이유", coverImg = R.drawable.img_album_exp2),
                    AlbumEntity(albumId = 3, title = "Supernova", singer = "aespa", coverImg = R.drawable.img_album_supernova),
                    AlbumEntity(albumId = 4, title = "NEXT LEVEL", singer = "aespa", coverImg = R.drawable.img_album_exp3),
                    AlbumEntity(albumId = 5, title = "BUTTER", singer = "BTS", coverImg = R.drawable.img_album_exp)
                )
                albumDao.insertAlbums(albumList)

                val insertedAlbums = albumDao.getAlbums()

                val songs = listOf(
                    SongEntity(title = "weekend", singer = "태연", second = 215, playTime = 0, isPlaying = false, music = "music1.mp3", coverImg = R.drawable.img_album_exp6, isLiked = false, albumId = insertedAlbums[0].albumId),
                    SongEntity(title = "weekend2", singer = "태연", second = 215, playTime = 0, isPlaying = false, music = "music1.mp3", coverImg = R.drawable.img_album_exp6, isLiked = false, albumId = insertedAlbums[0].albumId),
                    SongEntity(title = "lilac", singer = "아이유", second = 230, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_exp2, isLiked = false, albumId = insertedAlbums[1].albumId),
                    SongEntity(title = "lilac2", singer = "아이유", second = 230, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_exp2, isLiked = false, albumId = insertedAlbums[1].albumId),
                    SongEntity(title = "supernova", singer = "aespa", second = 200, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_supernova, isLiked = false, albumId = insertedAlbums[2].albumId),
                    SongEntity(title = "supernova2", singer = "aespa", second = 200, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_supernova, isLiked = false, albumId = insertedAlbums[2].albumId),
                    SongEntity(title = "next level", singer = "aespa", second = 200, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_supernova, isLiked = false, albumId = insertedAlbums[3].albumId),
                    SongEntity(title = "next level2", singer = "aespa", second = 200, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_supernova, isLiked = false, albumId = insertedAlbums[3].albumId),
                    SongEntity(title = "butter", singer = "BTS", second = 200, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_supernova, isLiked = false, albumId = insertedAlbums[4].albumId),
                    SongEntity(title = "butter2", singer = "BTS", second = 200, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_supernova, isLiked = false, albumId = insertedAlbums[4].albumId)
                )
                songDao.insertAll(songs)



                getSharedPreferences("song_pref", MODE_PRIVATE)
                    .edit().putInt("songId", 1).apply()


            }
        }
    }

    fun logAllSongs() {
        val db = SongDatabase.getDatabase(this)

        lifecycleScope.launch(Dispatchers.IO) {
            // 모든 곡을 가져오는 쿼리 실행
            val songList = db.songDao().getAll()

            // 각 곡에 대해 로그 출력
            songList.forEach { song ->
                Log.d("MainActivity", "Song ID: ${song.songId}, Title: ${song.title}, Artist: ${song.singer}")
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMain2ndBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        insertDummyData()

        logAllSongs()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(mBinding.myBtmNav, navController)

        MusicPlayerState.addPlayListener(playStateListener)
        MusicPlayerState.addProgressListener(progressListener)

        MusicPlayerState.addProgressListener { progress ->
            mBinding.timeSeekBar.progress = progress
        }

        mBinding.timeSeekBar.thumb = null
        mBinding.timeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayerState.setProgress(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        mBinding.miniPlayBtn.setOnClickListener {
            MusicPlayerState.togglePlay()
            Log.d("MainActivity", "현재 currentSongId: $currentSongId")
        }

        mBinding.miniPlayer.setOnClickListener {


            val db = SongDatabase.getDatabase(this)


            CoroutineScope(Dispatchers.IO).launch {
                val songId = MusicPlayerState.currentSongId

                if (songId != null) {
                    Log.d("MainActivity", "currentId is not null")
                    val song = db.songDao().getSongById(songId)
                    Log.d("MainActivity", "현재 currentSongId: ${MusicPlayerState.currentSongId}")

                    song?.let {
                        val intent = Intent(this@MainActivity_2nd, SongActivity::class.java).apply {
                            putExtra("songId", it.songId)
                            putExtra("songTitle", it.title)
                            putExtra("songArtist", it.singer)
                            putExtra("songCoverImg", it.coverImg ?: R.drawable.img_first_album_default)
                        }
                        startActivity(intent)
                    } ?: Log.d("MainActivity", "Song not found")
                } else {
                    Log.d("MainActivity", "currentId is null")
                }
            }
        }

        fun navigateToAdjacentSong(isNext: Boolean) {
            val db = SongDatabase.getDatabase(this)

            CoroutineScope(Dispatchers.IO).launch {
                val currentSongId = MusicPlayerState.currentSongId ?: return@launch

                Log.d("MainActivity", "현재 currentSongId: $currentSongId")

                val currentSong = db.songDao().getSongById(currentSongId) ?: return@launch
                val songList = db.songDao().getSongsByAlbumId(currentSong.albumId)

                val currentIndex = songList.indexOfFirst { it.songId == currentSongId }

                val newIndex = when {
                    isNext && currentIndex < songList.lastIndex -> currentIndex + 1
                    !isNext && currentIndex > 0 -> currentIndex - 1
                    else -> return@launch
                }

                val newSong = songList[newIndex]

                // DB 업데이트 (현재 곡 isPlaying = false, 새 곡 isPlaying = true)
                db.songDao().updateIsPlaying(currentSong.songId, false)
                db.songDao().updateIsPlaying(newSong.songId, true)

                // 전역 상태 업데이트
                MusicPlayerState.playNewSong(newSong.songId)

                // UI 업데이트는 반드시 메인 스레드에서
                runOnUiThread {
                    updateMiniPlayer(
                        title = newSong.title,
                        artist = newSong.singer,
                        isPlaying = true
                    )
                }
            }
        }


        mBinding.preBtn.setOnClickListener {
            navigateToAdjacentSong(isNext = false)
        }

        mBinding.nextBtn.setOnClickListener {
            navigateToAdjacentSong(isNext = true)
        }
    }

    // UI 업데이트 함수 통합
    fun updateMiniPlayer(title: String, artist: String, isPlaying: Boolean) {
        mBinding.miniSongTitle.text = title
        mBinding.miniSongArtist.text = artist

        // 플레이 버튼 이미지 업데이트
        mBinding.miniPlayBtn.setImageResource(
            if (isPlaying) R.drawable.btn_miniplay_pause
            else R.drawable.btn_miniplayer_play
        )

        // 전역 플레이 상태 업데이트
        MusicPlayerState.setPlayState(isPlaying)

        // MiniPlayer 표시
        mBinding.miniPlayer.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicPlayerState.removePlayListener(playStateListener)
    }
}
