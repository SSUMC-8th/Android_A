package com.example.umc_8th.flo_project

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVPAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> AlbumTrackFragment()
            1 -> AlbumDetailFragment()
            2 -> AlbumVideoFragment()
            else -> Fragment()
        }
    }

}