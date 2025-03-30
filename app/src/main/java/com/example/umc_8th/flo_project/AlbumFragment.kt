package com.example.umc_8th.flo_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAlbumBinding.inflate(inflater, container, false)
        binding.albumBackIv.setOnClickListener{
            (context as FloMainActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_frame, HomeFragment())
                .commitAllowingStateLoss()
        }

//        HomeFragment에서 넘겨받은 데이터(곡이름, 가수, 앨범 이미지 꺼내기)
        val album = arguments?.getSerializable("album")as?Album
        album?.let{
            binding.albumTitleTv.text=it.title
            binding.albumSingerNameTv.text=it.singer
            binding.albumAlbumimgIv.setImageResource(it.imageRes)
        }

//        앨범에서 탭뷰 구현
        val albumVPAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumVPAdapter

        val tabLayout = binding.albumContentTb
        TabLayoutMediator(tabLayout, binding.albumContentVp) { tab, position ->
            tab.text = when(position) {
                0 -> "수록곡"
                1 -> "상세정보"
                2 -> "영상"
                else -> ""
            }
        }.attach()

        return binding.root
    }
}