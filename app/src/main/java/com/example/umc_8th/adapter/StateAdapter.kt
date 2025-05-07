package com.example.umc_8th.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_8th.fragment.AlbumRecyclerFragment

class StateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return AlbumRecyclerFragment().apply {
            arguments = Bundle().apply {
                putInt("tabPosition", position)
            }
        }
    }
}
