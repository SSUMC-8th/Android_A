package com.example.umc_8th

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumPagerAdapter(fragment: Fragment, private val albumIndex: Int?) : FragmentStateAdapter(fragment) {
    private val fragments = listOf(
        TrackListFragment(),   // 수록곡
        AlbumDetailFragment(), // 상세정보
        AlbumVideoFragment()   // 영상
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val trackListFragment = TrackListFragment()
                trackListFragment.arguments = Bundle().apply {
                    putInt("albumIndex", albumIndex ?: -1)
                }
                trackListFragment
            }
            1 -> AlbumDetailFragment()
            2 -> AlbumVideoFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}