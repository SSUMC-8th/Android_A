package com.example.umc_8th.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_8th.MainActivity_2nd
import com.example.umc_8th.R
import com.example.umc_8th.adapter.SavedAdapter
import com.example.umc_8th.databinding.FragmentBottomDialogBinding
import com.example.umc_8th.databinding.FragmentSavedBinding
import com.example.umc_8th.viewmodel.SavedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class SavedFragment: Fragment() {
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    private val savedViewModel: SavedViewModel by viewModels()
    private lateinit var savedAdapter: SavedAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.selectAllBtn.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())

            // 바텀 다이얼로그 레이아웃을 바인딩으로 inflate
            val bottomBinding = FragmentBottomDialogBinding.inflate(layoutInflater)
            dialog.setContentView(bottomBinding.root)

            // 이미지와 텍스트 변경
            bottomBinding.saveOrDeleteImg.setImageResource(R.drawable.btn_editbar_delete) // 삭제용 아이콘
            bottomBinding.saveOrDeleteText.text = "삭제"

            // 다이얼로그 닫히면 미니플레이어 복원
            dialog.setOnDismissListener {
                (requireActivity() as? MainActivity_2nd)?.toggleBottomNavigation(true)
            }

            // 다이얼로그 표시 + 미니플레이어 숨기기
            dialog.show()
            (requireActivity() as? MainActivity_2nd)?.toggleBottomNavigation(false)
        }


        savedAdapter = SavedAdapter(mutableListOf(), object : SavedAdapter.OnItemClickListener {
            override fun onItemClick(songId: Int) {
                // 필요 시 클릭 이벤트 처리
            }
        })

        //리사이클러뷰 설정
        binding.savedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.savedRecyclerView.adapter = savedAdapter

        savedViewModel.savedList.observe(viewLifecycleOwner) { list ->
            savedAdapter.updateList(list)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}