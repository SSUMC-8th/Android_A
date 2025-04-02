package com.example.umc_8th.flo_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivityFloMainBinding

class FloMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityFloMainBinding
    companion object {const val STRING_INTENT_KEY ="message"}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyApp_Splash2)
        installSplashScreen()
        enableEdgeToEdge()
        binding=ActivityFloMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        BottomNav()

//        하단 미니 플레이어에서 SongAcitivitiy로 데이터 전달하고 띄우기
        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 0,60,false)
        binding.mainPlayerCl.setOnClickListener{
            val intent=Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
//            startActivity(intent)  <- 문제가 됐던 코드
            getResultText.launch(intent)
        }
    }

    private val getResultText =registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val returnString = result.data?.getStringExtra(STRING_INTENT_KEY)
            val toast = Toast.makeText(this, returnString, Toast.LENGTH_LONG)
            toast.show()
        }
    }
    private fun BottomNav(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeFragmnet->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lookFragment->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragmnet->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

}