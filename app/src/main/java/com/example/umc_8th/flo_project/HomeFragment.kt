package com.example.umc_8th.flo_project

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.umc_8th.MainActivity
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentHomeBinding
import java.util.TimerTask
import kotlin.concurrent.timer

class HomeFragment:Fragment() {
    lateinit var binding: FragmentHomeBinding
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var slideRunnable: Runnable
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

        //ViewPager, VPAdapter연결
        val pannelAdapter = PannelVPAdapter(this)
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_first_album_default))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_first_album_default))
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
}