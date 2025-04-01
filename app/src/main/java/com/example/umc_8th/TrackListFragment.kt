package com.example.umc_8th

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentTrackListBinding

class TrackListFragment : Fragment() {
    private var _binding: FragmentTrackListBinding? = null
    private val binding get() = _binding!!
    private var isToggleOn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 더미 데이터
        val albumIndex = arguments?.getInt("albumIndex")
        val trackList = albumList[albumIndex!!].trackList
        Log.d("TrackListFragment", "$albumIndex")

        val adapter = TrackListAdapter(trackList)
        binding.trackListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.trackListRv.adapter = adapter


        val favorToggle = binding.myFavorToggleIv
        favorToggle.setOnClickListener {
            if(!isToggleOn) {
                favorToggle.setImageResource(R.drawable.btn_toggle_on)
                isToggleOn = true
            }
            else{
                favorToggle.setImageResource(R.drawable.btn_toggle_off)
                isToggleOn = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}