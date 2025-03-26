package com.example.umc_8th.flo_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivityFloMainBinding

class FloMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityFloMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityFloMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.toString())
        binding.mainPlayerCl.setOnClickListener{
            val intent= Intent(this, SongActivity::class.java)
        }
        binding.mainPlayerCl.setOnClickListener{
            val intent=Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            startActivity(intent)
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