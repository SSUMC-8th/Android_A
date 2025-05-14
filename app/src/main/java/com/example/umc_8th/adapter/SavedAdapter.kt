package com.example.umc_8th.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_8th.MainActivity_2nd
import com.example.umc_8th.R
import com.example.umc_8th.SavedData
import com.example.umc_8th.databinding.ItemSavedBinding
import com.example.umc_8th.entity.SongEntity
import com.google.android.material.bottomsheet.BottomSheetDialog

class SavedAdapter(
    private var songList: MutableList<SongEntity>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SavedAdapter.SavedViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(songId: Int)
    }

    inner class SavedViewHolder(val binding: ItemSavedBinding) :
        RecyclerView.ViewHolder(binding.root) {

            //바텀다일로그
        private var isBottomDialogVisible = false
            //보관함 리사이클러뷰
        fun bind(song: SongEntity) {
            binding.savedName.text = song.title
            binding.savedArtist.text = song.singer
            binding.savedImg.setImageResource(song.coverImg ?: R.drawable.img_first_album_default)
            binding.root.setOnClickListener {
                listener.onItemClick(song.songId)

                if (isBottomDialogVisible) {
                    // 바텀 다이얼로그를 숨기고, 바텀 네비게이션 보이기
                    (it.context as? MainActivity_2nd)?.toggleBottomNavigation(true)
                    isBottomDialogVisible = false
                } else {
                    // 바텀 네비게이션 숨기고, 새로운 바텀 다이얼로그 바를 표시
                    (it.context as? MainActivity_2nd)?.toggleBottomNavigation(false)
                    showBottomDialog()  // 새로운 바텀 다이얼로그 표시
                    isBottomDialogVisible = true
                }
            }
        }
        private fun showBottomDialog() {
            // 바텀 다이얼로그 바를 생성하고 표시하는 로직
            val dialog = BottomSheetDialog(binding.root.context)
            dialog.setContentView(R.layout.fragment_bottom_dialog)  // 예시 레이아웃
            dialog.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        val binding = ItemSavedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedViewHolder(binding)
    }

    override fun getItemCount(): Int = songList.size

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
        holder.bind(songList[position])
    }

    fun updateList(newList: List<SongEntity>) {
        songList = newList.toMutableList()
        notifyDataSetChanged()
    }
}