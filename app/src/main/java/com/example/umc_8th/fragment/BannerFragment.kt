package com.example.umc_8th.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_8th.MainActivity_2nd
import com.example.umc_8th.R
import com.example.umc_8th.databinding.FragmentBannerBinding

class BannerFragment:Fragment() {

    private lateinit var binding: FragmentBannerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBannerBinding.inflate(inflater, container, false)

        val imageRes = arguments?.getInt("imageRes") ?: R.drawable.img_first_album_default
        val keyword = arguments?.getString("keyword") ?: "배너 키워드"
        val song1Title = arguments?.getString("song1Title") ?: "제목"
        val song1Artist = arguments?.getString("song1Artist") ?: "가수"
        val song2Title = arguments?.getString("song2Title") ?: "제목"
        val song2Artist = arguments?.getString("song2Artist") ?: "가수"

        // UI에 데이터 설정
        binding.bannerImg.setImageResource(imageRes)
        binding.bannerKeyword.text = keyword
        binding.song1Title.text = song1Title
        binding.song1Artist.text = song1Artist
        binding.song2Title.text = song2Title
        binding.song2Artist.text = song2Artist

        //미니플레이어로 데이터 전달
        binding.bannerPlayBtn.setOnClickListener {
            val title = binding.song1Title.text.toString()
            val artist = binding.song1Artist.text.toString()

            val mainActivity = activity as? MainActivity_2nd
            mainActivity?.updateMiniPlayer(title, artist, true)
        }


        return binding.root
    }

    companion object {
        fun newInstance(imageRes: Int, keyword: String, song1Title: String, song1Artist: String, song2Title: String, song2Artist: String): BannerFragment {
            val fragment = BannerFragment()
            val args = Bundle()
            args.putInt("imageRes", imageRes)
            args.putString("keyword", keyword)
            args.putString("song1Title", song1Title)
            args.putString("song1Artist", song1Artist)
            args.putString("song2Title", song2Title)
            args.putString("song2Artist", song2Artist)
            fragment.arguments = args
            return fragment
        }
    }
}