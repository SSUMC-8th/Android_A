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
            tvArtist.text = song.artist
            ivAlbum.setImageResource(song.albumResId)

            itemSavedSwitch.isChecked = song.isSelected // 🔸 모델 상태 기반으로 설정

            itemSavedSwitch.setOnCheckedChangeListener(null) // 🔸 리스너 초기화 (중복 방지)
            itemSavedSwitch.setOnCheckedChangeListener { _, isChecked ->
                song.isSelected = isChecked // 🔸 상태 변경 시 모델 업데이트
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
