package com.example.umc_8th

import com.example.umc_8th.MusicPlayerState

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.umc_8th.databinding.ActivityMain2ndBinding


class MainActivity_2nd : AppCompatActivity(){

    private lateinit var mBinding : ActivityMain2ndBinding
    val TAG: String = "로그"

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMain2ndBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment

        //네비게이션을 컨트롤러
        val navController = navHostFragment.navController
        //바텀네비게이션뷰와 묶어줌
        NavigationUI.setupWithNavController(mBinding.myBtmNav, navController)

        MusicPlayerState.addPlayListener(playStateListener)

        mBinding.miniPlayBtn.setOnClickListener {
            MusicPlayerState.togglePlay()
        }

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
