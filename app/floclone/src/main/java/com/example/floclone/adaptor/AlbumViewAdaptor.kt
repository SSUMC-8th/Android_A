package com.example.floclone.adaptor

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.floclone.Album_jungbo_Fragment
import com.example.floclone.Album_songlist_Fragment
import com.example.floclone.Album_video_Fragment
import com.example.floclone.Song
import com.example.floclone.database.Album
import com.example.floclone.database.Song as SongDB

//인자를 받자
class AlbumViewAdaptor(fragment : Fragment,
                       private val album: Album,
                       private val songList: List<SongDB>): FragmentStateAdapter(fragment) {
    //일단 tab 2개
    override fun getItemCount(): Int {
        return 3;
    }


    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment =  when (position){
            0 -> Album_songlist_Fragment()
            1 -> Album_jungbo_Fragment()
            2 -> Album_video_Fragment()
            else -> Album_songlist_Fragment()
        }

        // Song 객체를 Bundle에 담아 전달
        fragment.arguments = Bundle().apply {
            putParcelable("album", album)
            putParcelableArrayList("songs", ArrayList(songList))
        }

        //인자를 넣어 전달하자
        return fragment;
    }

}