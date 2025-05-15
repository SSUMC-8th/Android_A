package com.example.umc_8th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentSavedSongsBinding

class SavedSongsFragment : Fragment() {

    private var _binding: FragmentSavedSongsBinding? = null
    private val binding get() = _binding!!

    private lateinit var songAdapter: SavedSongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sampleSongs = mutableListOf(
            Song("결론적으로", "SPARKY", R.drawable.img_album_exp6),
            Song("별거 없던 그 하루로", "임정정", R.drawable.img_album_exp6),
            Song("선물 (White Day)", "코튼캔디", R.drawable.img_album_exp6),
            Song("Bad Boy (PREP Remix)", "Red Velvet", R.drawable.img_album_exp6),
            Song("Always Me", "2am", R.drawable.img_album_exp6),
            Song("잘 가라니", "2am", R.drawable.img_album_exp6),

        )

        songAdapter = SavedSongAdapter(sampleSongs) { song ->
            songAdapter.removeSong(song)
        }

        binding.savedSongRv.layoutManager = LinearLayoutManager(requireContext())
        binding.savedSongRv.adapter = songAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
