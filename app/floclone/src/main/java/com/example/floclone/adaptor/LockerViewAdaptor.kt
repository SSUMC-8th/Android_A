package com.example.floclone.adaptor

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerViewAdaptor(fragment : Fragment): FragmentStateAdapter(fragment) {
    //일단 tab 3개
    override fun getItemCount(): Int {
        return 3;
    }


    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> Locker_SavesongFragment()
            1 -> Locker_SongFileFragment()
            2 -> Locker_SavealbumFragment()
            else -> Locker_SavesongFragment()
        }
    }

}