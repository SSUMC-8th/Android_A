package com.example.umc_8th

import android.content.Intent
import android.os.Bundle
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


class MainActivity_2nd : AppCompatActivity(){

    private lateinit var mBinding : ActivityMain2ndBinding

    private val playStateListener: (Boolean) -> Unit = { isPlaying ->
        mBinding.miniPlayBtn.setImageResource(
            if (isPlaying) R.drawable.btn_miniplay_pause
            else R.drawable.btn_miniplayer_play
        )
    }

    //seekbar리스너 변수 설정
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
                // 앨범 리스트 생성
                val albumList = listOf(
                    AlbumEntity(albumId = 1, title = "Weekend", singer = "태연", coverImg = R.drawable.img_album_exp6),
                    AlbumEntity(albumId = 2, title = "Lilac", singer = "아이유", coverImg = R.drawable.img_album_exp2),
                    AlbumEntity(albumId = 3, title = "Supernova", singer = "aespa", coverImg = R.drawable.img_album_supernova),
                    AlbumEntity(albumId = 4, title = "NEXT LEVEL", singer = "aespa", coverImg = R.drawable.img_album_exp3),
                    AlbumEntity(albumId = 5, title = "BUTTER", singer = "BTS", coverImg = R.drawable.img_album_exp)
                )
                // 앨범 삽입
                albumDao.insertAlbums(albumList)

                // 삽입된 앨범 가져오기
                val insertedAlbums = albumDao.getAlbums()

                // 곡 리스트 생성 (songId는 자동으로 생성되므로 title만 String으로 지정)
                val songs = listOf(
                    SongEntity(title = "weekend", singer = "태연", second = 215, playTime = 0, isPlaying = false, music = "music1.mp3", coverImg = R.drawable.img_album_exp6, isLiked = false, albumId = insertedAlbums[0].albumId),
                    SongEntity(title = "weekend2", singer = "태연", second = 215, playTime = 0, isPlaying = false, music = "music1.mp3", coverImg = R.drawable.img_album_exp6, isLiked = false, albumId = insertedAlbums[0].albumId),
                    SongEntity(title = "lilac", singer = "아이유", second = 230, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_exp2, isLiked = false, albumId = insertedAlbums[1].albumId),
                    SongEntity(title = "supernova", singer = "aespa", second = 200, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_supernova, isLiked = false, albumId = insertedAlbums[2].albumId),
                    SongEntity(title = "next level", singer = "aespa", second = 200, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_supernova, isLiked = false, albumId = insertedAlbums[3].albumId),
                    SongEntity(title = "butter", singer = "BTS", second = 200, playTime = 0, isPlaying = false, music = "music2.mp3", coverImg = R.drawable.img_album_supernova, isLiked = false, albumId = insertedAlbums[4].albumId)
                )
                // 곡 삽입
                songDao.insertAll(songs)

                // SharedPreferences에 첫 곡 ID 저장
                getSharedPreferences("song_pref", MODE_PRIVATE)
                    .edit().putInt("songId", 1).apply()
            }
        }

}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMain2ndBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //DB 호출
        insertDummyData()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment

        //네비게이션을 컨트롤러
        val navController = navHostFragment.navController
        //바텀네비게이션뷰와 묶어줌
        NavigationUI.setupWithNavController(mBinding.myBtmNav, navController)

        MusicPlayerState.addPlayListener(playStateListener)

        //진행도 리스너
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

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 필요하다면 일시정지 시킬 수도 있음
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 드래그가 끝났을 때, 자동 진행은 그대로 유지됨
            }
        })

        mBinding.miniPlayBtn.setOnClickListener {
            MusicPlayerState.togglePlay()
        }

        mBinding.miniPlayer.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("songId", MusicPlayerState.currentSongId)
                putExtra("songTitle", mBinding.miniSongTitle.text.toString())
                putExtra("songArtist", mBinding.miniSongArtist.text.toString())
                //이미지 추가로 넣기(추후)
            }
            startActivity(intent)
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
