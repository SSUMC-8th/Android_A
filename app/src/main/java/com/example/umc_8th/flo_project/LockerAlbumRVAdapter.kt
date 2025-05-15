package com.example.umc_8th.flo_project

import android.annotation.SuppressLint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_8th.databinding.ItemLockerAlbumBinding
//import umc.study.umc_8th.databinding.ItemLockerAlbumBinding

class LockerAlbumRVAdapter () : RecyclerView.Adapter<LockerAlbumRVAdapter.ViewHolder>() {
    private val switchStatus = SparseBooleanArray()
    private val songs = ArrayList<Song>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LockerAlbumRVAdapter.ViewHolder {
        val binding: ItemLockerAlbumBinding = ItemLockerAlbumBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerAlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])

        holder.binding.itemLockerAlbumMoreIv.setOnClickListener {
            itemClickListener.onRemoveAlbum(songs[position].id) // 좋아요 취소로 업데이트하는 메서드
            removeSong(position) // 현재 화면에서 아이템을 제거
        }

//        val switch =  holder.binding.switchRV
//        switch.isChecked = switchStatus[position]
//        switch.setOnClickListener {
//            if (switch.isChecked) {
//                switchStatus.put(position, true)
//            }
//            else {
//                switchStatus.put(position, false)
//            }
//
//            notifyItemChanged(position)
//        }
    }

    override fun getItemCount(): Int = songs.size

    inner class ViewHolder(val binding: ItemLockerAlbumBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(song : Song){
            binding.itemLockerAlbumTitleTv.text = song.title
            binding.itemLockerAlbumSingerTv.text = song.singer
            binding.itemLockerAlbumCoverImgIv.setImageResource(song.coverImg!!)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(album : Album)
        fun onRemoveAlbum(songId: Int)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

}