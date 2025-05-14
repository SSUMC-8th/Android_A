package com.example.umc_8th.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_8th.R
import com.example.umc_8th.SavedData
import com.example.umc_8th.databinding.ItemSavedBinding
import com.example.umc_8th.entity.SongEntity

class SavedAdapter(
    private var songList: MutableList<SongEntity>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SavedAdapter.SavedViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(songId: Int)
    }

    inner class SavedViewHolder(val binding: ItemSavedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: SongEntity) {
            binding.savedName.text = song.title
            binding.savedArtist.text = song.singer
            binding.savedImg.setImageResource(song.coverImg ?: R.drawable.img_first_album_default)
            binding.root.setOnClickListener {
                listener.onItemClick(song.songId)
            }
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


//class SavedAdapter(
//    private val savedList: MutableList<SavedData>,
//    private val onItemClickListener: OnItemClickListener
//) : RecyclerView.Adapter<SavedAdapter.SavedViewHolder>() {
//
//    interface OnItemClickListener {
//        fun onItemClick(position: Int)
//    }
//
//    inner class SavedViewHolder(private val binding: ItemSavedBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(savedData: SavedData) {
//            binding.savedImg.setImageResource(savedData.savedImg)
//            binding.savedName.text = savedData.savedName
//            binding.savedArtist.text = savedData.savedArtist
//
//            binding.moreBtn.setOnClickListener {
//                val pos = bindingAdapterPosition
//                if (pos != RecyclerView.NO_POSITION) {
//                    onItemClickListener.onItemClick(pos)
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
//        val binding = ItemSavedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return SavedViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
//        holder.bind(savedList[position])
//    }
//
//    override fun getItemCount(): Int = savedList.size
//
//
//    fun updateList(newList: List<SavedData>) {
//        savedList.clear()
//        savedList.addAll(newList)
//        notifyDataSetChanged()
//
//    }
//
//}
