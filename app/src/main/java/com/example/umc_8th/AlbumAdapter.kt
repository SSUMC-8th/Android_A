package com.example.umc_8th

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R

class AlbumAdapter(
    private val onAlbumClick: (Album) -> Unit // 클릭 이벤트 처리
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val albumImage: ImageView = view.findViewById(R.id.item_album_cover_img_iv)
        private val albumTitle: TextView = view.findViewById(R.id.item_album_title_tv)
        private val albumArtist: TextView = view.findViewById(R.id.item_album_singer_tv)

        fun bind(album: Album) {
            albumImage.setImageResource(album.coverImage)
            albumTitle.text = album.title
            albumArtist.text = album.artist

            itemView.setOnClickListener {
                onAlbumClick(album) // 클릭한 앨범 데이터 전달
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albumList[position])
    }

    override fun getItemCount() = albumList.size
}