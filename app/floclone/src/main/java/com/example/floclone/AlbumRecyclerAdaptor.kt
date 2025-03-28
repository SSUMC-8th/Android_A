package com.example.floclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


//데이터-뷰 연결 + View 생성(각 요소들), View 재사용, 클릭 처리 등
//private val onImageClick: (Song) -> Unit) :
// ㄴ onImageClick 변수를 생성 이 타입은 song 객체를 인자로 받아 return X
class AlbumRecyclerAdaptor(private val songList:List<Song>, private val onImageClick: (Song) -> Unit):
        RecyclerView.Adapter<AlbumRecyclerAdaptor.ViewHolder>() {

    // ViewHolder(하나의 아이템을 구성하는 뷰들의 모음)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imv_albumCover_home_recycler)
        val titleView: TextView = itemView.findViewById(R.id.tv_songName_home_recycler)
        val artistView: TextView = itemView.findViewById(R.id.tv_artistName_home_recycler)
    }

    //아이템에 대한 View 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_recyclerview, parent, false)
        return ViewHolder(view)
    }

    //데이터를 View에 연경
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.imageView.setImageResource(song.image)
        holder.titleView.text = song.title
        holder.artistView.text = song.artist

        //onclicklistener
        holder.imageView.setOnClickListener{
            onImageClick(song)
        }
    }

    override fun getItemCount(): Int = songList.size



}