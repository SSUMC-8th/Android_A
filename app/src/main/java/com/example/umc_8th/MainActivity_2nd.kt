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
                val albumList = listOf(
                    AlbumEntity("Weekend", "태연", R.drawable.img_album_exp6),
                    AlbumEntity("Lilac", "아이유", R.drawable.img_album_exp2),
                    AlbumEntity("Supernova","aespa",R.drawable.img_album_supernova),
                    AlbumEntity("NEXT LEVEL","aespa",R.drawable.img_album_exp3),
                    AlbumEntity("BUTTER","BTS",R.drawable.img_album_exp)
                )
                albumDao.insertAlbums(albumList)

                // 앨범 id 가져오기
                val insertedAlbums = albumDao.getAlbums()

                val songs = listOf(
                    SongEntity("weekend", "태연", 215, 0, false, "music1.mp3", R.drawable.img_album_exp6, false, insertedAlbums[0].id),
                    SongEntity("weekend2", "태연", 215, 0, false, "music1.mp3", R.drawable.img_album_exp6, false, insertedAlbums[0].id),
                    SongEntity("lilac", "아이유", 230, 0, false, "music2.mp3", R.drawable.img_album_exp2, false, insertedAlbums[1].id),
                    SongEntity("supernova", "aespa", 200, 0, false, "music2.mp3", R.drawable.img_album_supernova, false, insertedAlbums[2].id),
                    SongEntity("next level", "aespa", 200, 0, false, "music2.mp3", R.drawable.img_album_supernova, false, insertedAlbums[3].id),
                    SongEntity("butter", "BTS", 200, 0, false, "music2.mp3", R.drawable.img_album_supernova, false, insertedAlbums[4].id)
                )
                songDao.insertAll(songs)

                // SharedPref에 첫 곡 id 저장
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
