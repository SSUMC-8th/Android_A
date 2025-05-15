package com.example.umc_8th

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.databinding.ItemSavedBinding

class SavedSongAdapter(
    private val songList: MutableList<Song>,
    private val onDeleteClick: (Song) -> Unit
) : RecyclerView.Adapter<SavedSongAdapter.SongViewHolder>() {

    inner class SongViewHolder(val binding: ItemSavedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemSavedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songList[position]
        holder.binding.apply {
            tvTitle.text = song.title
            tvArtist.text = song.singer
            ivAlbum.setImageResource(song.id)

            // 더보기 버튼 클릭 시 삭제 콜백 호출
            btnMore.setOnClickListener {
                onDeleteClick(song)
            }
        }
    }

    override fun getItemCount() = songList.size

    // 외부에서 호출 가능한 삭제 함수
    fun removeSong(song: Song) {
        val index = songList.indexOf(song)
        if (index != -1) {
            songList.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
