package com.example.umc_8th

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2 // 탭 개수

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedSongsFragment() // 저장한 곡 Fragment
            1 -> MusicFilesFragment() // 음악 파일 Fragment
            //2 -> SavedAlbumsFragment() // 저장된 앨범 Fragment
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}