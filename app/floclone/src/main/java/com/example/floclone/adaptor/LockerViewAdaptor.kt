package com.example.floclone.adaptor

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerViewAdaptor(fragment : Fragment): FragmentStateAdapter(fragment) {
    //일단 tab 2개
    override fun getItemCount(): Int {
        return 2;
    }


    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> Locker_SavesongFragment()
            1 -> Locker_SongFileFragment()
            else -> Locker_SavesongFragment()
        }
    }

}