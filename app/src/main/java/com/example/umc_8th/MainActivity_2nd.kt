package com.example.umc_8th

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
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
        mBinding.miniPlayBtn.setImageResource(
            if (isPlaying) R.drawable.btn_miniplay_pause
            else R.drawable.btn_miniplayer_play
        )
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
                    SongEntity(title = "supernova", singer = "aespa", second = 200, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_supernova, isLiked = false, albumId = insertedAlbums[2].albumId),
                    SongEntity(title = "next level", singer = "aespa", second = 200, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_supernova, isLiked = false, albumId = insertedAlbums[3].albumId),
                    SongEntity(title = "butter", singer = "BTS", second = 200, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_supernova, isLiked = false, albumId = insertedAlbums[4].albumId)
                )
                songDao.insertAll(songs)

                getSharedPreferences("song_pref", MODE_PRIVATE)
                    .edit().putInt("songId", 1).apply()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMain2ndBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        insertDummyData()

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
        }

        mBinding.miniPlayer.setOnClickListener {
            Log.d("MainActivity", "Mini player clicked!")  // 로그 추가

            // DB를 통해 직접 데이터를 가져오는 대신, 이미 선택된 currentSongId로 곡 정보를 가져오기
            val db = SongDatabase.getDatabase(this)

            CoroutineScope(Dispatchers.IO).launch {
                // currentSongId가 null이 아니면 해당 songId로 곡을 찾아서 SongActivity로 전달
                val songId = MusicPlayerState.currentSongId

                if (songId != null) {
                    Log.d("MainActivity", "currentId is not null")  // 로그 추가
                    val song = db.songDao().getSongById(songId)  // currentSongId를 사용하여 곡 정보 가져오기
                    Log.d("MainActivity", "현재 currentSongId: ${MusicPlayerState.currentSongId}")

                    // 곡 정보를 가져와서 Intent로 전달
                    song?.let {
                        val intent = Intent(this@MainActivity_2nd, SongActivity::class.java).apply {
                            putExtra("songId", it.songId)
                            putExtra("songTitle", it.title)
                            putExtra("songArtist", it.singer)
                            putExtra("songCoverImg", it.coverImg ?: R.drawable.img_first_album_default) // 기본 이미지 fallback
                        }
                        startActivity(intent)
                    } ?: Log.d("MainActivity", "Song not found")  // song이 없을 경우 로그
                } else {
                    Log.d("MainActivity", "currentId is null")  // currentSongId가 null일 때의 처리
                }
            }
        }







    }

    fun updateMiniPlayer(title: String, artist: String, isPlaying: Boolean) {
        mBinding.miniSongTitle.text = title
        mBinding.miniSongArtist.text = artist

        MusicPlayerState.setPlayState(isPlaying)

        mBinding.miniPlayer.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicPlayerState.removePlayListener(playStateListener)
    }
}
