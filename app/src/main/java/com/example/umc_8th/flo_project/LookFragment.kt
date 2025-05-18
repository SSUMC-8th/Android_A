package com.example.umc_8th.flo_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_8th.R
import com.example.umc_8th.databinding.FragmentLockerBinding
import com.example.umc_8th.databinding.FragmentLookBinding
import com.google.android.material.tabs.TabLayoutMediator
//import umc.study.umc_8th.R
//import umc.study.umc_8th.databinding.FragmentLockerBinding

class LookFragment: Fragment(){
    lateinit var binding: FragmentLookBinding
    lateinit var scrollView : ScrollView
    private val information = arrayListOf("저장한 곡", "음악 파일", "저장앨범")
    private lateinit var songDB: SongDatabase
    private lateinit var chartBtn : Button
    private lateinit var videoBtn : Button
    private lateinit var genreBtn : Button
    private lateinit var situationBtn : Button
    private lateinit var audioBtn : Button
    private lateinit var buttonList: List<Button>

    private lateinit var chartTv : TextView
    private lateinit var textList: List<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLookBinding.inflate(inflater, container, false)
//        binding = FragmentLockerBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!
//        val lockerAdapter = LockerVPAdapter(this)
//        binding.lockerContentVp.adapter = lockerAdapter
//        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
//            tab.text = information[position]
//        }.attach()

        // 스크롤 뷰 초기화
        scrollView = binding.lookSv

        // 버튼 초기화
        chartBtn = binding.lookChartBtn
        videoBtn =  binding.lookVideoBtn
        genreBtn =  binding.lookGenreBtn
        situationBtn =  binding.lookSituationBtn
        audioBtn =  binding.lookAudioBtn
        buttonList = listOf(chartBtn, videoBtn, genreBtn, situationBtn, audioBtn)

        // 텍스트 초기화
        chartTv = binding.lookChartTv
        setButtonClickListeners()
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        val recyclerView = binding.lookChartSongRv
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val lookAlbumRVAdapter = LockerAlbumRVAdapter()

        binding.lookChartSongRv.adapter = lookAlbumRVAdapter
        lookAlbumRVAdapter.addSongs(songDB.songDao().getSongs() as ArrayList<Song>)
    }

    private fun setButtonClickListeners() {
        for (i in buttonList.indices) {
            val button = buttonList[i]

            button.setOnClickListener {
                initButton(i)
            }
        }
    }

    private fun initButton(idx : Int) {
        for(presentBtn : Button in buttonList) {
            if(presentBtn == buttonList[idx]) {
                presentBtn.setBackgroundResource(R.drawable.selected_button)
            } else {
                presentBtn.setBackgroundResource(R.drawable.not_selected_button)
            }
        }
        scrollView.smoothScrollTo(0, textList[idx].top)
    }
}