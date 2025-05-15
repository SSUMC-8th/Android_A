package com.example.umc_8th.flo_project

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.umc_8th.R
import com.example.umc_8th.databinding.FragmentHomeBinding
import com.google.gson.Gson
//import umc.study.umc_8th.R
//import umc.study.umc_8th.databinding.FragmentHomeBinding
import java.util.TimerTask
import kotlin.concurrent.timer

class HomeFragment:Fragment(), CommunicationInterface {
    lateinit var binding: FragmentHomeBinding
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var slideRunnable: Runnable
    private var albumDatas = ArrayList<Album>()
    lateinit var songDB: SongDatabase

     override fun sendData(album: Album) { // MainActivity의 UI를 업데이트하기 위해 사용하는 메서드
        if (activity is FloMainActivity) {
            val activity = activity as FloMainActivity
            activity.updateMainPlayerCl(album)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater, container, false)
//        val albumList= listOf(
//            Album("LILAC", "아이유 (IU)", R.drawable.img_album_exp2),
//            Album("Butter", "BTS", R.drawable.img_album_exp),
//            Album("NextLevel", "에스파", R.drawable.img_album_exp3),
//            Album("Weekend", "태연", R.drawable.img_album_exp4),
//            Album("Baam", "모모랜드", R.drawable.img_album_exp5),
//            Album("Drama", "에스타", R.drawable.img_album_drama)
//        )

//        albumDatas.apply {
//            add(Album(id = 1, title = "LILAC", singer = "아이유 (IU)", coverImage = R.drawable.img_album_exp2))
//            add(Album(id = 2, title = "Butter", singer = "BTS", coverImage = R.drawable.img_album_exp))
//            add(Album(id = 3, title = "NextLevel", singer = "에스파", coverImage = R.drawable.img_album_exp3))
//            add(Album(id = 4, title = "Weekend", singer = "태연", coverImage = R.drawable.img_album_exp4))
//            add(Album(id = 5, title = "BBoom BBoom", singer = "모모랜드", coverImage = R.drawable.img_album_exp5))
//            add(Album(id = 6, title = "Drama", singer = "에스타", coverImage = R.drawable.img_album_drama))
//        }
        DummyAlbum()
        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums())

        //ViewPager, VPAdapter연결
        val pannelAdapter = PannelVPAdapter(this)
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_first_album_default))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_first_album_default))
        binding.homeFragTop.adapter=pannelAdapter
        binding.homeFragTop.orientation=ViewPager2.ORIENTATION_HORIZONTAL

        //viewpager랑 indicator 연결
        binding.homePannelIndicator.setViewPager(binding.homeFragTop)
        binding.homePannelIndicator.setViewPager(binding.homeBannerVp)


//        HomeFragment에서 배너 연결
        val bannerAdapter = BannerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter =bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        autoSlide(bannerAdapter)

//        앨범 목록 RecyclerView설정
//        val adapter = AlbumAdapter(albumList){album->
//            val fragment =AlbumFragment()
//            val bundle = Bundle()
//            bundle.putSerializable("album", album)
//            fragment.arguments=bundle
//
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.main_frame, fragment)
//                .addToBackStack(null)
//                .commit()
//        }
        val albumRVAdapter = AlbumAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager=
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        //      앨범 데이터 연결
        albumRVAdapter.setItemClickListener(object : AlbumAdapter.OnItemClickListener {
            override fun onItemClick(album: Album) {
                changeToAlbumFragment(album)
            }

            override fun onPlayAlbum(album: Album) {
                sendData(album)
            }

        })


        return binding.root
    }

    //    private fun autoSlide(adapter: PannelVPAdapter){
//        slideRunnable=object:Runnable{
//            override fun run(){
//                val nextItem= binding.homeFragTop.currentItem+1
//                binding.homeFragTop.currentItem=if(nextItem<adapter.itemCount) nextItem else 0
//                handler.postDelayed(this, 4000)
//            }
//        }
//        handler.postDelayed(slideRunnable, 4000)
//    }
    private fun changeToAlbumFragment(album: Album) {
        (context as FloMainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albuToJson = gson.toJson(album)
                    putString("album", albuToJson)
                }
            })
            .commitAllowingStateLoss()
    }

    private fun autoSlide(adapter: BannerAdapter) {
        slideRunnable = object : Runnable {
            override fun run() {
                val nextItem = binding.homeBannerVp.currentItem + 1
                binding.homeBannerVp.currentItem = if (nextItem < adapter.itemCount) nextItem else 0
                handler.postDelayed(this, 4000)
            }
        }
        handler.postDelayed(slideRunnable, 4000)
    }

    private fun DummyAlbum(){
        val songDB = SongDatabase.getInstance(requireActivity())!!
        val songs = songDB.albumDao().getAlbums()

        if (songs.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                1,
                "IU 5th Album 'LILAC'",
                "아이유 (IU)",
                R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "Butter",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "Next Level",
                "에스파 (AESPA)",
                R.drawable.img_album_exp3
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "Music Boy",
                "뮤직 보이 (Music Boy)",
                R.drawable.img_album_exp4,
            )
        )


        songDB.albumDao().insert(
            Album(
                5,
                "Great",
                "모모랜드 (MOMOLAND)",
                R.drawable.img_album_exp5
            )
        )
    }
}