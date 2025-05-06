package com.example.umc_8th.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_8th.AlbumItem
import com.example.umc_8th.databinding.AlbumRecyclerviewBinding

class AlbumAdapter(private val albumList: List<AlbumItem>,
                   private val onPlayClick: (String, String) -> Unit,
                   private val onItemClick: (AlbumItem) -> Unit ) :
    RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(val binding: AlbumRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)
//    {
//        fun bind(album: AlbumItem) {
//            binding.albumImg.setImageResource(album.albumImage)
//            binding.albumName.text = album.albumName
//            binding.artistName.text = album.artistName
//
//            binding.root.setOnClickListener {
//                onItemClick(album) // 클릭 시 함수 실행
//            }
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding =
            AlbumRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albumList[position]

        with(holder.binding) {
            albumImg.setImageResource(album.albumImage)
            albumName.text = album.albumName
            artistName.text = album.artistName

            // 기존 아이템 전체 클릭
            root.setOnClickListener {
                onItemClick(album)
            }

            // 🎯 play_btn 클릭 시 제목과 가수만 전달
            playBtn.setOnClickListener {
                onPlayClick(album.albumName, album.artistName)
            }
        }


    }
    override fun getItemCount(): Int = albumList.size
}