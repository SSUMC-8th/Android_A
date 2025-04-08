package com.example.umc_8th

import com.example.umc_8th.MusicPlayerState

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMain2ndBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment

        //네비게이션을 컨트롤러
        val navController = navHostFragment.navController
        //바텀네비게이션뷰와 묶어줌
        NavigationUI.setupWithNavController(mBinding.myBtmNav, navController)

//       // miniPlayer 버튼 클릭 시 토글, +곡 세팅 안되면 버튼 비활성
//        mBinding.miniPlayBtn.setOnClickListener {
//            val isPlaying = it.tag == "playing"
//            setMiniPlayerButtonState(!isPlaying)
//        }

        MusicPlayerState.addListener(playStateListener)

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
        MusicPlayerState.removeListener(playStateListener)
    }
}


//package com.example.umc_8th
//
//import com.example.umc_8th.MusicPlayerState
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.fragment.NavHostFragment
//import androidx.navigation.ui.NavigationUI
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.umc_8th.databinding.ActivityMain2ndBinding
//
//
//class MainActivity_2nd : AppCompatActivity(){
//
//
//    private lateinit var mBinding : ActivityMain2ndBinding
//    val TAG: String = "로그"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        mBinding = ActivityMain2ndBinding.inflate(layoutInflater)
//
//        setContentView(mBinding.root)
//
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment
//
//        //네비게이션을 컨트롤러
//        val navController = navHostFragment.navController
//        //바텀네비게이션뷰와 묶어줌
//        NavigationUI.setupWithNavController(mBinding.myBtmNav, navController)
//
//       // miniPlayer 버튼 클릭 시 토글, +곡 세팅 안되면 버튼 비활성
//        mBinding.miniPlayBtn.setOnClickListener {
//            val isPlaying = it.tag == "playing"
//            setMiniPlayerButtonState(!isPlaying)
//        }
//
//        mBinding.miniPlayer.setOnClickListener {
//            val intent = Intent(this, SongActivity::class.java).apply {
//                putExtra("songTitle", mBinding.miniSongTitle.text.toString())
//                putExtra("songArtist", mBinding.miniSongArtist.text.toString())
//                //이미지 추가로 넣기(추후)
//            }
//            startActivity(intent)
//        }
//
//
//
//    }
//
//    private fun setMiniPlayerButtonState(isPlaying: Boolean) {
//        if (isPlaying) {
//            mBinding.miniPlayBtn.setImageResource(R.drawable.btn_miniplay_pause)
//            mBinding.miniPlayBtn.tag = "playing"
//        } else {
//            mBinding.miniPlayBtn.setImageResource(R.drawable.btn_miniplayer_play)
//            mBinding.miniPlayBtn.tag = "paused"
//        }
//    }
//
//    fun updateMiniPlayer(title: String, artist: String, isPlaying: Boolean) {
//        mBinding.miniSongTitle.text = title
//        mBinding.miniSongArtist.text = artist
//
//        if (isPlaying) {
//            mBinding.miniPlayBtn.setImageResource(R.drawable.btn_miniplay_pause)
//            mBinding.miniPlayBtn.tag = "playing"
//        } else {
//            mBinding.miniPlayBtn.setImageResource(R.drawable.btn_miniplayer_play)
//            mBinding.miniPlayBtn.tag = "paused"
//        }
//
//        mBinding.miniPlayer.visibility = View.VISIBLE
//    }
//
//
//}