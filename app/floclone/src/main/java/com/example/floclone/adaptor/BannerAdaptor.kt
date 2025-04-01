package com.example.floclone.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.Banner
import com.example.floclone.R
import com.example.floclone.Song

class BannerAdaptor (private val bannerList:List<Banner>):
    RecyclerView.Adapter<BannerAdaptor.ViewHolder>() {

    // ViewHolder(하나의 아이템을 구성하는 뷰들의 모음)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jarmokView: TextView = itemView.findViewById(R.id.tv_jaemok_banner)
        val songsinfoView: TextView = itemView.findViewById(R.id.tv_songinfo_banner)
        val firstsongName: TextView = itemView.findViewById(R.id.tv_firstsongName_banner)
        val firstartistName: TextView = itemView.findViewById(R.id.tv_firstsongArtist_banner)
        val secondsongName: TextView = itemView.findViewById(R.id.tv_secondsongName_banner)
        val secondartistName: TextView = itemView.findViewById(R.id.tv_secondsongArtist_banner)
        val firstImageView: ImageView = itemView.findViewById(R.id.imv_first_banner)
        val secondImageView: ImageView = itemView.findViewById(R.id.imv_second_banner)
    }

    //아이템에 대한 View 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner_homefragment, parent, false)
        return ViewHolder(view)
    }

    //데이터를 View에 연경
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val now = bannerList[position]
        //이거는 나중에 처리
        holder.itemView.setBackgroundColor(now.color.toString().toColorInt())
        holder.jarmokView.text = now.bannerTitle.toString()
        holder.songsinfoView.text = now.info.toString()
        holder.firstsongName.text = now.song1.title.toString()
        holder.firstartistName.text = now.song1.artist.toString()
        holder.secondsongName.text = now.song2.title.toString()
        holder.secondartistName.text = now.song2.artist.toString()
        holder.firstImageView.setImageResource(now.song1.image)
        holder.secondImageView.setImageResource(now.song2.image)
    }

    override fun getItemCount(): Int = bannerList.size

}