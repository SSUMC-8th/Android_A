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
import com.google.android.material.tabs.TabLayoutMediator
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentLockerBinding

class LockerFragment : Fragment(){
    private lateinit var binding: FragmentLockerBinding
    private lateinit var lockerAdapter: LockerPageAdapter
    private val info = arrayListOf("저장한 곡", "음악파일")
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

        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
//            tab.text = info[position]
            val tabText = TextView(requireContext()).apply {
                text = info[position]
                gravity = Gravity.CENTER
                setSingleLine(true)
                setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_LabelLarge)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_unselected)) // 초기 상태는 unselected 색
            }
            tab.customView = tabText
        }.attach()
        binding.lockerContentVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                for (i in 0 until binding.lockerContentTb.tabCount) {
                    val textView = binding.lockerContentTb.getTabAt(i)?.customView as? TextView
                    val colorRes = if (i == position) R.color.tab_selected else R.color.tab_unselected
                    textView?.setTextColor(ContextCompat.getColor(requireContext(), colorRes))
                }
            }
        })
        return binding.root
    }
}