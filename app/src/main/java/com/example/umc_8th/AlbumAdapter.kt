package com.example.umc_8th

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_8th.databinding.AlbumRecyclerviewBinding

class AlbumAdapter(private val albumList: List<AlbumModel>,
                   private val onItemClick: (AlbumModel) -> Unit ) :
    RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(private val binding: AlbumRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: AlbumModel) {
            binding.albumImg.setImageResource(album.albumImage)
            binding.albumName.text = album.albumName
            binding.artistName.text = album.artistName

            binding.root.setOnClickListener {
                onItemClick(album) // 클릭 시 함수 실행
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = AlbumRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albumList[position])
    }

    override fun getItemCount(): Int = albumList.size
}
