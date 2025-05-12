package com.example.umc_8th.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_8th.BannerAlbumItem
import com.example.umc_8th.databinding.BannerAlbumRecyclerviewBinding

class BannerAlbumAdapter(
    private val items: List<BannerAlbumItem>,
    private val onItemClick: ((BannerAlbumItem) -> Unit)? = null // 선택형 파라미터로 수정
) : RecyclerView.Adapter<BannerAlbumAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(val binding: BannerAlbumRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = BannerAlbumRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            bannerAlbumImg.setImageResource(item.imageRes)
            bannerAlbumSongName.text = item.title
            bannerAlbumSongArtist.text = item.artist

            // 클릭 리스너 - 배너 클릭시 이동
            root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
