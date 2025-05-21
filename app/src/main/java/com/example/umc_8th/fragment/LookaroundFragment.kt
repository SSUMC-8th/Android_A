package com.example.umc_8th.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_8th.adapter.CategoryAdapter
import com.example.umc_8th.databinding.FragmentLookaroundBinding

class LookaroundFragment:Fragment() {
    private var mBinding: FragmentLookaroundBinding? = null
    private val binding get() = mBinding!!

    private val categoryList = listOf(
        "차트", "영상", "장르", "상황", "분위기", "오디오"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentLookaroundBinding.inflate(inflater,container,false)
        val adapter = CategoryAdapter(categoryList) { category ->

        }

        binding.categoryRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoryRecyclerView.adapter = adapter

        return binding.root

    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}