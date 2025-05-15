package com.example.umc_8th.flo_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_8th.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator
//import umc.study.umc_8th.R
//import umc.study.umc_8th.databinding.FragmentLockerBinding

class LookFragment: Fragment(){
    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf("저장한 곡", "음악 파일")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()
        return binding.root
    }
}