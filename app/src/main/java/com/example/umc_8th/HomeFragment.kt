
package com.example.umc_8th

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var binding: FragmentHomeBinding

//    private val albumList = listOf(
//        Album("LILAC", "아이유 (IU)", R.drawable.img_album_exp2),
//        Album("Title 2", "Artist 2", R.drawable.img_album_exp4),
//        Album("Title 3", "Artist 3", R.drawable.img_album_exp3)
//    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 배너 이미지 목록
        val adbannerlist = listOf(
            R.drawable.img_home_viewpager_exp,
            R.drawable.img_home_viewpager_exp2,
        )

        val topbannerlist = listOf(
            R.drawable.img_first_album_default,
            R.drawable.img_potcast_exp,
            R.drawable.discovery_banner_aos
        )

        // ViewPager2 어댑터 설정
        val adAdapter = AdBannerAdapter(adbannerlist)
        binding = FragmentHomeBinding.bind(view)
        binding.adBannerVp.adapter = adAdapter

        val topAdapter = TopBannerAdapter(topbannerlist)
        binding.topBannerVp.adapter = topAdapter

        val tophandler = Handler(Looper.getMainLooper())
        val toprunnable = object : Runnable {
            override fun run() {
                val topcurrentItem = binding.topBannerVp.currentItem
                val nextItem = (topcurrentItem + 1) % topbannerlist.size
                binding.topBannerVp.setCurrentItem(nextItem, true)
                tophandler.postDelayed(this, 3000)
            }
        }
        tophandler.postDelayed(toprunnable, 3000)

        recyclerView = view.findViewById(R.id.home_today_music_album_rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        albumAdapter = AlbumAdapter(
            albumList = albumList,
            onAlbumClick = { album ->
                // 앨범 전체를 클릭했을 때: 앨범 상세 화면으로 이동
                openAlbumFragment(album)
            },
            onPlayClick = { album ->
                val track = album.trackList.firstOrNull()
                Log.d("HomeFragment", "Clicked track: $track")
                if (track != null) {
                    val mediaPlayer = MediaPlayer.create(requireContext(), album.trackList[0].musicResId)
                    val duration = mediaPlayer.duration
                    mediaPlayer.release()
                    Log.d("HomeFragment", "Duration: $duration")
                    val intent = Intent("com.example.umc_8th.ALBUM_PLAY").apply {
                        putExtra("title", track.title)
                        putExtra("artist", track.artist)
                        putExtra("progress", 0)
                        putExtra("duration", duration)
                    }
                    requireContext().sendBroadcast(intent)
                }
            }

        )
        recyclerView.adapter = albumAdapter
    }

    private fun openAlbumFragment(album: Album) {
        val bundle = Bundle().apply {
            putString("albumTitle", album.title)
            putString("albumArtist", album.artist)
            putInt("albumCover", album.coverImage)
            putInt("albumListIndex", albumList.indexOf(album))
        }

        findNavController().navigate(R.id.action_homeFragment_to_albumFragment, bundle)
    }
}