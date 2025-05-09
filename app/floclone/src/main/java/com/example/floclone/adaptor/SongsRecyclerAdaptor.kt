package com.example.floclone.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.R
import com.example.floclone.Song
import com.example.floclone.database.Song as SongDB

class SongsRecyclerAdaptor(private val songList:List<SongDB>):
    RecyclerView.Adapter<SongsRecyclerAdaptor.ViewHolder>()
{

    // ViewHolder(하나의 아이템을 구성하는 뷰들의 모음)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sunseView: TextView = itemView.findViewById(R.id.tv_sunseSong_album_recycler)
        val titleView: TextView = itemView.findViewById(R.id.tv_songName_album_recycler)
        val artistView: TextView = itemView.findViewById(R.id.tv_artistName_album_recycler)
    }

    //아이템에 대한 View 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_albumfragment_recyclerview, parent, false)
        return ViewHolder(view)
    }

    //데이터를 View에 연경
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.sunseView.text = String.format("%02d", position+1)
        holder.titleView.text = song.title
        holder.artistView.text = song.singer

    }

    override fun getItemCount(): Int = songList.size
}