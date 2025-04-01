package com.example.umc_8th

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ItemBannerBinding
import umc.study.umc_8th.databinding.ItemTrackBinding

class TrackListAdapter(private val trackList: List<Track>) :
    RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {

    class TrackViewHolder(private val binding: ItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track, position: Int) {
            binding.trackNumTv.text = (position + 1).toString()
            binding.trackTitleTv.text = track.title
            binding.trackArtistTv.text = track.artist

            binding.trackPlayIv
                .setOnClickListener {
                Toast.makeText(binding.root.context, "${track.title} 재생", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position], position)
    }

    override fun getItemCount(): Int = trackList.size
}