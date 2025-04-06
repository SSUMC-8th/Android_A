package com.example.umc_8th.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_8th.AlbumAdapter
import com.example.umc_8th.AlbumItem
import com.example.umc_8th.R
import androidx.navigation.fragment.findNavController
import com.example.umc_8th.BannerAdapter
import com.example.umc_8th.BannerItem
import com.example.umc_8th.StateAdapter
import com.example.umc_8th.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 더미 데이터 추가
        val albumList = listOf(
            AlbumItem(R.drawable.img_album_exp, "Butter", "방탄소년단(BTS)"),
            AlbumItem(R.drawable.img_album_exp2, "LILAC", "아이유(IU)"),
            AlbumItem(R.drawable.img_album_exp3, "Album Three", "Artist C"),
            AlbumItem(R.drawable.img_album_exp4, "Album Four", "Artist D"),
            AlbumItem(R.drawable.img_album_exp5, "Album Five", "Artist E")
        )

        // 어댑터 설정
        albumAdapter = AlbumAdapter(albumList) { album ->
            val bundle = Bundle().apply {
                putString("title", album.albumName)
                putString("artist", album.artistName)
                putInt("imageRes", album.albumImage)
            }

            findNavController().navigate(R.id.action_homeFragment_to_albumFragment, bundle) // 🔥 navigate 사용
        }
        //binding.albumRecyclerView.adapter = albumAdapter

        val bannerList = listOf(
            BannerItem(R.drawable.img_first_album_default, "추천 플레이리스트", "노래1", "가수1", "노래2", "가수2"),
            BannerItem(R.drawable.img_first_album_default, "인기 앨범", "노래3", "가수3", "노래4", "가수4"),
            BannerItem(R.drawable.img_first_album_default, "새로운 음악", "노래5", "가수5", "노래6", "가수6")
        )

        val banneradapter = BannerAdapter(this, bannerList)
        binding.bannerPager.adapter = banneradapter
        binding.dotsIndicator.attachTo(binding.bannerPager)

        val stateAdapter = StateAdapter(this)
        binding.viewPager.adapter = stateAdapter
        binding.viewPager.isUserInputEnabled = false //스와이프 금지

        // TabLayout과 ViewPager 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "종합"
                1 -> "국내"
                else -> "해외"
            }
        }.attach()
        binding.tabLayout.setSelectedTabIndicator(null)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}