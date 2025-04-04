package com.example.umc_8th

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_8th.fragment.ListedFragment
import com.example.umc_8th.fragment.InfoFragment

class NewSongAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2  // 탭 개수 (저장한 곡, 음악 파일)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ListedFragment()  // 저장한 곡 Fragment
            1 -> InfoFragment()  // 음악 파일 Fragment
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

}
