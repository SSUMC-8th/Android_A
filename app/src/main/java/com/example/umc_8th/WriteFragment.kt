package com.example.umc_8th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import umc.study.umc_8th.databinding.FragmentWriteBinding

class WriteFragment:Fragment() {
    private lateinit var binding: FragmentWriteBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentWriteBinding.inflate(layoutInflater)
        return binding.root
    }
}