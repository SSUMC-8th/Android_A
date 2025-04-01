package com.example.umc_8th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import umc.study.umc_8th.R

class AlbumFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val albumTitle = arguments?.getString("albumTitle")
        val albumArtist = arguments?.getString("albumArtist")
        val albumCover = arguments?.getInt("albumCover")

        val titleTextView: TextView = view.findViewById(R.id.album_detail_title)
        val artistTextView: TextView = view.findViewById(R.id.album_detail_artist)
        val coverImageView: ImageView = view.findViewById(R.id.album_detail_cover)

        titleTextView.text = albumTitle
        artistTextView.text = albumArtist
        coverImageView.setImageResource(albumCover ?: R.drawable.img_album_exp2)

        // 뒤로 가기 버튼 설정
        val backButton: ImageView = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}