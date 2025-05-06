package com.example.umc_8th.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_8th.fragment.FileFragment
import com.example.umc_8th.fragment.SavedFragment

class StorageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2  // 탭 개수 (저장한 곡, 음악 파일)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedFragment()  // 저장한 곡 Fragment
            1 -> FileFragment()  // 음악 파일 Fragment
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
