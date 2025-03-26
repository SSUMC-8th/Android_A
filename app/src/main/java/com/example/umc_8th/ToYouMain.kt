package com.example.umc_8th

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivityToYouMainBinding

class ToYouMain : AppCompatActivity() {
    private lateinit var binding: ActivityToYouMainBinding
    private var currentFragmentId = R.id.bottom_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_to_you_main)
        binding= ActivityToYouMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState==null){
            replaceFragment(ToYouHomeFragment())
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.bottom_home ->{
                    replaceFragmentWithAnimation(ToYouHomeFragment(), R.id.bottom_home)
                }
                R.id.bottom_write ->{
                    replaceFragmentWithAnimation(WriteFragment(), R.id.bottom_write)
                }
                R.id.bottom_calendar ->{
                    replaceFragmentWithAnimation(CalendarFragment(), R.id.bottom_calendar)
                }
                R.id.bottom_profile ->{
                    replaceFragmentWithAnimation(ProfileFragment(), R.id.bottom_profile)
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slid_in_right,
                R.anim.slid_out_left)
            .replace(R.id.main_container, fragment)
            .commit()
    }

    private fun replaceFragmentWithAnimation(fragment: Fragment, newItemId: Int) {
        val transaction = supportFragmentManager.beginTransaction()

        if (newItemId > currentFragmentId) {
            // 오른쪽 → 왼쪽
            transaction.setCustomAnimations(
                R.anim.slid_in_right,
                R.anim.slid_out_left
            )
        } else if (newItemId < currentFragmentId) {
            // 왼쪽 → 오른쪽
            transaction.setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
        transaction.replace(R.id.main_container, fragment)
            .commit()

        currentFragmentId = newItemId
    }
}