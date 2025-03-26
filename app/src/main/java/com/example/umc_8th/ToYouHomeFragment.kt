package com.example.umc_8th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import umc.study.umc_8th.databinding.FragmentToYouHomeBinding

class ToYouHomeFragment:Fragment() {
    private lateinit var binding: FragmentToYouHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentToYouHomeBinding.inflate(layoutInflater)
        return binding.root
    }
}