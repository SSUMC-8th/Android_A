package com.example.umc_8th

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_8th.fragment.BannerFragment

class BannerAdapter(fragment: Fragment, private val banners: List<BannerItem>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = banners.size

    override fun createFragment(position: Int): Fragment {
        val banner = banners[position]
        return BannerFragment.newInstance(
            banner.imageRes, banner.keyword,
            banner.song1Title, banner.song1Artist,
            banner.song2Title, banner.song2Artist
        )
    }
}
