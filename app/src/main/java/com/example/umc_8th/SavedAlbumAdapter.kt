package com.example.umc_8th

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R

class SavedAlbumAdapter :
    ListAdapter<Album, SavedAlbumAdapter.SavedAlbumViewHolder>(diffCallback) {

    inner class SavedAlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val albumCover: ImageView = itemView.findViewById(R.id.album_cover)
        private val albumTitle: TextView = itemView.findViewById(R.id.album_title)
        private val albumArtist: TextView = itemView.findViewById(R.id.album_artist)
        //private val albumDateGenre: TextView = itemView.findViewById(R.id.album_date_genre)

        fun bind(album: Album) {
            albumCover.setImageResource(album.coverImage)
            albumTitle.text = album.title
            albumArtist.text = album.artist
            //albumDateGenre.text = "${album.releaseDate} | ${album.genre}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedAlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.album_saved_item, parent, false)
        return SavedAlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedAlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem.albumId == newItem.albumId
            }

            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem == newItem
            }
        }
    }
}
