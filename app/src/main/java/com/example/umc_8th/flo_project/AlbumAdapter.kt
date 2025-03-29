package com.example.umc_8th.flo_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.databinding.ItemAlbumBinding

class AlbumAdapter(private val albumList:List<Album>, private val itemClick: (Album) -> Unit) :
    RecyclerView.Adapter<AlbumAdapter.ViewHolder>(){
        inner class ViewHolder(private val binding: ItemAlbumBinding):
                RecyclerView.ViewHolder(binding.root){
                    fun bind(album: Album){
                        binding.itemAlbumTitleTv.text = album.title
                        binding.itemAlbumSingerTv.text = album.singer
                        binding.itemAlbumCoverImgIv.setImageResource(album.imageRes)

                        binding.root.setOnClickListener{
                            itemClick(album)
                        }
                    }
                }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=albumList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
    }

}