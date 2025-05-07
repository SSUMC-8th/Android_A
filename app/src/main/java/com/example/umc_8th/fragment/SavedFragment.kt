package com.example.umc_8th.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_8th.R
import com.example.umc_8th.SavedData
import com.example.umc_8th.adapter.SavedAdapter
import com.example.umc_8th.databinding.FragmentSavedBinding
import com.example.umc_8th.viewmodel.SavedViewModel

class SavedFragment:Fragment(){
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    //뷰모델 추가
    private val savedViewModel: SavedViewModel by viewModels()
    private lateinit var savedAdapter: SavedAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        savedAdapter = SavedAdapter(mutableListOf(), object : SavedAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                savedViewModel.removeItem(position)  // 아이템 클릭 시 아이템 제거
            }
        })

        binding.savedRecyclerView.layoutManager =LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
        binding.savedRecyclerView.adapter = savedAdapter

        savedViewModel.savedList.observe(viewLifecycleOwner) { list ->
            savedAdapter.updateList(list)  // 데이터 변경 시 어댑터에 업데이트
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}