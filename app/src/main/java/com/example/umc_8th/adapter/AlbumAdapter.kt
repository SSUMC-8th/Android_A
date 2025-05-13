package com.example.umc_8th.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_8th.AlbumItem
import com.example.umc_8th.databinding.AlbumRecyclerviewBinding


class AlbumAdapter(
    private val albumList: List<AlbumItem>,
    private val onPlayClick: (albumId: Int, title: String, artist: String) -> Unit,
    private val onItemClick: (AlbumItem) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(val binding: AlbumRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)

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

            root.setOnClickListener {
                onItemClick(album)
            }

            playBtn.setOnClickListener {
                onPlayClick(album.albumId, album.albumName, album.artistName)
            }
        }
    }

    override fun getItemCount(): Int = albumList.size
}