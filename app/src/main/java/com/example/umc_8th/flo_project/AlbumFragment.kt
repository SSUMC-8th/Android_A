package com.example.umc_8th.flo_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding
    private var isMixed=false
    private val originalOrder = mutableListOf<View>()

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

//        HomeFragment에서 넘겨받은 데이터 꺼내기
        val album = arguments?.getSerializable("album")as?Album
        album?.let{
            binding.albumTitleTv.text=it.title
            binding.albumSingerNameTv.text=it.singer
        }

//        취향 mix버튼
        val musicListLayout = binding.songMusicListLayout
        if (originalOrder.isEmpty()) {
            for (i in 0 until musicListLayout.childCount) {
                originalOrder.add(musicListLayout.getChildAt(i))
            }
        }

        binding.songMixoffTg.setOnClickListener{
            binding.songMixoffTg.visibility=View.GONE
            binding.songMixonTg.visibility=View.VISIBLE

            val shuffledList = originalOrder.shuffled()

            musicListLayout.removeAllViews()
            for (view in shuffledList) {
                musicListLayout.addView(view)
            }
        }
        binding.songMixonTg.setOnClickListener{
            binding.songMixoffTg.visibility=View.VISIBLE
            binding.songMixonTg.visibility=View.GONE
            musicListLayout.removeAllViews()
            for (view in originalOrder) {
                musicListLayout.addView(view)
            }
        }

//      각 노래에 클릭 리스너
        binding.songLalacLayout.setOnClickListener{
            Toast.makeText(activity, "Lilac", Toast.LENGTH_LONG).show()
        }
        binding.songFluLayout.setOnClickListener{
            Toast.makeText(activity, "Flu", Toast.LENGTH_LONG).show()
        }
        binding.songCoinLayout.setOnClickListener {
            Toast.makeText(activity, "Coin", Toast.LENGTH_LONG).show()
        }
        binding.songSpringLayout.setOnClickListener {
            Toast.makeText(activity, "봄 안녕", Toast.LENGTH_LONG).show()
        }
        binding.songCelebrityLayout.setOnClickListener {
            Toast.makeText(activity, "Celebrity", Toast.LENGTH_LONG).show()
        }
        binding.songSingLayout.setOnClickListener {
            Toast.makeText(activity, "돌림노래", Toast.LENGTH_LONG).show()
        }
        return binding.root
    }
}