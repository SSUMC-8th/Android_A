package com.example.umc_8th.flo_project

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerAdapter(fragment: Fragment) :FragmentStateAdapter(fragment) {
    private val fragmentList:ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int {
        TODO("Not yet implemented")
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        TODO("Not yet implemented")
        return fragmentList[position]
    }
    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size-1)
    }
}