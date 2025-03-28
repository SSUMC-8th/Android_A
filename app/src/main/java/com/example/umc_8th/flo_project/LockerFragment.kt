package com.example.umc_8th.flo_project

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentLockerBinding

class LockerFragment : Fragment(){
    private lateinit var binding: FragmentLockerBinding
    private lateinit var lockerAdapter: LockerPageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        lockerAdapter = LockerPageAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter

        // TabLayout + ViewPager2 연결
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = when (position) {
                0 -> "저장한 곡"
                1 -> "음악파일"
                else -> ""
            }
        }.attach()

        return binding.root
    }
}