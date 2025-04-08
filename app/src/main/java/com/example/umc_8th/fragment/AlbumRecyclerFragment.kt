package com.example.umc_8th.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_8th.AlbumAdapter
import com.example.umc_8th.AlbumItem
import com.example.umc_8th.R
import com.example.umc_8th.databinding.FragmentAlbumRecyclerBinding
import androidx.navigation.fragment.findNavController

class AlbumRecyclerFragment : Fragment() {

    private var _binding: FragmentAlbumRecyclerBinding? = null
    private val binding get() = _binding!!

    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 📌 더미 데이터 추가 (여기에 position 값에 따라 다르게 넣을 수도 있음)
        val albumList = listOf(
            AlbumItem(R.drawable.img_album_exp, "Butter", "방탄소년단(BTS)"),
            AlbumItem(R.drawable.img_album_exp2, "LILAC", "아이유(IU)"),
            AlbumItem(R.drawable.img_album_exp3, "Album Three", "Artist C"),
            AlbumItem(R.drawable.img_album_exp4, "Album Four", "Artist D"),
            AlbumItem(R.drawable.img_album_exp5, "Album Five", "Artist E")
        )

        // 어댑터 설정 + 클릭 이벤트 추가 ✅
        albumAdapter = AlbumAdapter(albumList) { album ->
            val bundle = Bundle().apply {
                putString("title", album.albumName)
                putString("artist", album.artistName)
                putInt("imageRes", album.albumImage)
            }
            findNavController().navigate(R.id.action_homeFragment_to_albumFragment, bundle)
        }

        // RecyclerView 설정 ✅
        //binding.albumRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.albumRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.albumRecyclerView.adapter = albumAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
