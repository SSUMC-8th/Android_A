package com.example.umc_8th.flo_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_8th.MainActivity
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentHomeBinding

class HomeFragment:Fragment() {
    lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater, container, false)
        val albumList= listOf(
            Album("LILAC", "아이유 (IU)", R.drawable.img_album_exp2),
            Album("Butter", "BTS", R.drawable.img_album_exp),
            Album("NextLevel", "에스파", R.drawable.img_album_exp3),
            Album("Weekend", "태연", R.drawable.img_album_exp4),
            Album("Baam", "모모랜드", R.drawable.img_album_exp5),
            Album("Drama", "에스타", R.drawable.img_album_drama)
        )
        val adapter = AlbumAdapter(albumList){album->
            val fragment =AlbumFragment()
            val bundle = Bundle()
            bundle.putSerializable("album", album)
            fragment.arguments=bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frame, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.homeTodayMusicAlbumRv.adapter = adapter

        binding.homeTodayMusicAlbumRv.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }
}