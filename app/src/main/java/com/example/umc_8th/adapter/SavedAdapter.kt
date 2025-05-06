package com.example.umc_8th.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_8th.SavedData
import com.example.umc_8th.databinding.ItemSavedBinding



class SavedAdapter(private val savedList: List<SavedData>): RecyclerView.Adapter<SavedAdapter.SavedViewHolder>() {
    inner class SavedViewHolder(private val binding: ItemSavedBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(savedData: SavedData){
            binding.savedImg.setImageResource(savedData.savedImg)
            binding.savedName.text = savedData.savedName
            binding.savedArtist.text = savedData.savedArtist
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        val binding =ItemSavedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
        holder.bind(savedList[position])
    }

    override fun getItemCount(): Int = savedList.size
}