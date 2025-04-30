package com.example.floclone

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job


class MainActivity : AppCompatActivity() {

    private lateinit var clHomeplayer : ConstraintLayout
    private var playCheck = false;

    //코루틴으로 Media재생 및 넘겨주기
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private var updateJob: Job? = null
    private var currentPosition: Int = 0
    private var durationSong: Int = 0

    private lateinit var btnPlay : ImageButton
    private lateinit var btnPause : ImageButton


    //registerForActivityResult
    private val songResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val returnString = result.data?.getStringExtra("title")
            Toast.makeText(this, "Title: $returnString", Toast.LENGTH_SHORT).show()

            //songActivity에서 받아온 거 연동
            val returnedPosition = result.data?.getIntExtra("currentPosition", 0) ?: 0
            val returnedPlayCheck = result.data?.getBooleanExtra("playCheck", false) ?: false

            //연동하기
            currentPosition = returnedPosition
            seekBar.progress = currentPosition
            mediaPlayer.seekTo(currentPosition)

            playCheck = returnedPlayCheck
            if(playCheck) {
                playMusic()
                btnPlay.visibility = View.GONE
                btnPause.visibility = View.VISIBLE
            }
            else{
                stopMusic()
                btnPlay.visibility = View.VISIBLE
                btnPause.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var title = findViewById<TextView>(R.id.tv_homeplayer_title).text.toString()
        var artist = findViewById<TextView>(R.id.tv_homeplayer_artist).text.toString()

        btnPlay = findViewById<ImageButton>(R.id.btn_homeplayer_play);
        btnPause = findViewById<ImageButton>(R.id.btn_homeplayer_pause);

        //홈 뮤직 정의
        handleMusic()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.ct_home, HomeFragment())
                .commit()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnv_home)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                //홈 화면
                R.id.navigation_homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.ct_home, HomeFragment())
                        .commit()
                    true
                }
                //보관함
                R.id.navigation_storeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.ct_home, LockerFragment())
                        .commit()
                    true
                }
                // 다른 메뉴 항목에 대해서도 필요하면 추가
                else -> false
            }
        }

        btnPlay.setOnClickListener {
            btnPlay.visibility = View.GONE
            btnPause.visibility = View.VISIBLE
            playCheck = true
            playMusic()
        }
        btnPause.setOnClickListener {
            btnPlay.visibility = View.VISIBLE
            btnPause.visibility = View.GONE
            playCheck = false
            stopMusic()
        }

        //플레이 바랑 연결해서 노래 액티비티로 이동
        clHomeplayer = findViewById(R.id.cl_homeplayer)
        clHomeplayer.setOnClickListener({
            //코루틴 정리
            if(mediaPlayer.isPlaying){
                stopMusic()
            }
            updateJob?.cancel()

            //intent 처리
            val intent = Intent(this, SongActivity::class.java)
            val songPlay = SongPlay(title, artist, playCheck,
                currentPosition, durationSong,
                "例えば僕ら二人 煌めく映画のように",
                "出会いなおせたらどうしたい")
            intent.putExtra("songPlay", songPlay)
            //intent를 만들고 startActivity 대신 registerForActivityResult를 수행
            songResultLauncher.launch(intent)
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }


    private fun handleMusic(){
        mediaPlayer = MediaPlayer.create(this, R.raw.lady_kenshi_yonezu)
        seekBar = findViewById<SeekBar>(R.id.sbar_HomeActivity)
        seekBar.max = mediaPlayer.duration
        durationSong = mediaPlayer.duration
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // 사용자가 직접 조작했다면 미디어 플레이어 위치 변경
                if(fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // 사용자가 터치를 시작할 때 필요한 동작을 구현
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // 사용자가 터치를 종료할 때 필요한 동작을 구현
            }
        })
    }
    
    private fun playMusic(){
        if(!mediaPlayer.isPlaying) {
            mediaPlayer.start() //재생
        }
        updateSeekbar()
    }

    private fun stopMusic(){
        if(mediaPlayer.isPlaying) {
            mediaPlayer.pause() //멈춤
        }
    }

    //코루틴으로 updateSeekbar 설정
    private fun updateSeekbar(){
        //코루틴 이미 실행중이면 더 실행 X
        if(updateJob?.isActive == true) return

        updateJob = lifecycleScope.launch(Dispatchers.Main){
            while(mediaPlayer.isPlaying){
                delay(1000)
                //만약 플레이 멈추면 이 코루틴 종료
                if(!mediaPlayer.isPlaying) break

                currentPosition = mediaPlayer.currentPosition
                seekBar.progress = currentPosition
            }
        }

    }

}