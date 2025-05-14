package com.example.umc_8th.flo_project

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.umc_8th.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator
//import umc.study.umc_8th.R
//import umc.study.umc_8th.databinding.FragmentLockerBinding

class LockerFragment : Fragment(){
    private lateinit var binding: FragmentLockerBinding
    private lateinit var lockerAdapter: LockerPageAdapter
    private val information = arrayListOf("저장한 곡", "음악파일")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
//        lockerAdapter = LockerPageAdapter(this)
//        binding.lockerContentVp.adapter = lockerAdapter

        val lockerPageAdapter = LockerPageAdapter(this)
        binding.lockerContentVp.adapter=lockerPageAdapter
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        val bottomSheetFragment = BottomSheetFragment()

        binding.lockerSelectAllTv.setOnClickListener {
            bottomSheetFragment.show(requireFragmentManager(), "BottomSheetDialog")
        }

        return binding.root
    }
}