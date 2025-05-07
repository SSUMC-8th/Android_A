package com.example.umc_8th.fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.umc_8th.adapter.NewSongAdapter
import com.example.umc_8th.R
import com.example.umc_8th.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔹 HomeFragment에서 전달받은 데이터 가져오기
        val albumTitle = arguments?.getString("title") ?: "제목 없음"
        val artist = arguments?.getString("artist") ?: "아티스트 정보 없음"
        val imageRes = arguments?.getInt("imageRes") ?: R.drawable.ic_launcher_foreground

        // 🔹 UI 업데이트 (binding 사용)
        binding.albumTitle.text = albumTitle
        binding.albumArtist.text = artist
        binding.albumImg.setImageResource(imageRes)

        val newsongAdapter = NewSongAdapter(this)  // 어댑터 이름 변경
        binding.newSongPager.adapter = newsongAdapter

        TabLayoutMediator(binding.newSongTab, binding.newSongPager) { tab, position ->
            tab.text = when (position) {
                0 -> "수록곡"
                1 -> "상세정보"
                else -> ""
            }
        }.attach()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
