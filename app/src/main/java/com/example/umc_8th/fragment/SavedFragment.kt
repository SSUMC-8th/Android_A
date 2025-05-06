package com.example.umc_8th.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_8th.R
import com.example.umc_8th.SavedData
import com.example.umc_8th.adapter.SavedAdapter
import com.example.umc_8th.databinding.FragmentSavedBinding

class SavedFragment:Fragment(){
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //음악 리스트
        val savedList = mutableListOf(
            SavedData(
                savedImg = R.drawable.img_album_lovewinsall,
                savedName = "love wins all",
                savedArtist = "아이유"
            ),
            SavedData(
                savedImg = R.drawable.img_album_supernova,
                savedName = "supernova",
                savedArtist = "aespa"
            ),
            SavedData(
                savedImg = R.drawable.img_album_drama,
                savedName = "drama",
                savedArtist = "aespa"
            ),
            SavedData(
                savedImg = R.drawable.img_album_exp3,
                savedName = "next level",
                savedArtist = "aespa"
            ),
            SavedData(
                savedImg = R.drawable.img_album_exp6,
                savedName = "weekend",
                savedArtist = "태연"
            ),
            SavedData(
                savedImg = R.drawable.img_album_lovewinsall,
                savedName = "love wins all",
                savedArtist = "아이유"
            ),
            SavedData(
                savedImg = R.drawable.img_album_supernova,
                savedName = "supernova",
                savedArtist = "aespa"
            ),
            SavedData(
                savedImg = R.drawable.img_album_drama,
                savedName = "drama",
                savedArtist = "aespa"
            ),
            SavedData(
                savedImg = R.drawable.img_album_exp3,
                savedName = "next level",
                savedArtist = "aespa"
            ),
            SavedData(
                savedImg = R.drawable.img_album_exp6,
                savedName = "weekend",
                savedArtist = "태연"
            )
        )

        lateinit var savedAdapter: SavedAdapter //먼저 선언

        savedAdapter = SavedAdapter(savedList, object : SavedAdapter.OnItemClickListener {
            override fun onItemClick(position: Int){
                savedAdapter.removeItem(position)
            }
        })
        
        binding.savedRecyclerView.layoutManager =LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
        binding.savedRecyclerView.adapter = savedAdapter
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}