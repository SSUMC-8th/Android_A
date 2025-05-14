
package com.example.umc_8th

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R

class AlbumAdapter(
    private val albumList: List<Album>,
    private val onAlbumClick: (Album) -> Unit,
    private val onPlayClick: (Album) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val albumImage: ImageView = view.findViewById(R.id.item_album_cover_img_iv)
        private val albumTitle: TextView = view.findViewById(R.id.item_album_title_tv)
        private val albumArtist: TextView = view.findViewById(R.id.item_album_singer_tv)
        private val playButton: ImageView = view.findViewById(R.id.item_album_play_img_iv)

        fun bind(album: Album) {
            albumImage.setImageResource(album.coverImage)
            albumTitle.text = album.title
            albumArtist.text = album.artist

            // 전체 아이템 클릭 시
            itemView.setOnClickListener {
                onAlbumClick(album)
            }

            // 재생 버튼만 클릭 시
            playButton.setOnClickListener {
                onPlayClick(album)
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