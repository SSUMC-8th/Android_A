package com.example.floclone.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.floclone.R
import com.example.floclone.Song
import com.example.floclone.adaptor.AlbumRecyclerAdaptor.ViewHolder

class SavesongRecyclerAdaptor(
    private val songList:ArrayList<Song>
    ): RecyclerView.Adapter<SavesongRecyclerAdaptor.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imv_songImage_savesong_recycler)
        val titleView: TextView = itemView.findViewById(R.id.tv_songName_savesong_recycler)
        val artistView: TextView = itemView.findViewById(R.id.tv_artistName_savesong_recycler)
        val btnPlay: ImageButton = itemView.findViewById(R.id.btn_play_savesong_recycler)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete_savesong_recycler)
        val btnPause: ImageButton = itemView.findViewById(R.id.btn_pause_savesong_recycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lockerfragment_savesong_recycleriew, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.imageView.setImageResource(song.image)

        //Glide.with(holder.itemView.context)
        //    .load(song.image)
        //    .into(holder.imageView)

        holder.titleView.text = song.title
        holder.artistView.text = song.artist

        //song 객체 boolean 상태에 따라 보여주기를 다르게 설정
        if (song.isPlaying) {
            holder.btnPlay.visibility = View.GONE
            holder.btnPause.visibility = View.VISIBLE
        } else {
            holder.btnPlay.visibility = View.VISIBLE
            holder.btnPause.visibility = View.GONE
        }

        holder.btnDelete.setOnClickListener {
            //그냥 삭제 시 position 정리 후에도 문제 발생 가능
            //songList.removeAt(position)
            //notifyItemRemoved(position)

            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION && pos < songList.size) {
                songList.removeAt(pos)
                notifyItemRemoved(pos) //삭제 후 애니메이션 반영
                notifyItemRangeChanged(pos, songList.size - pos)
            }

        }

        holder.btnPlay.setOnClickListener {
            song.isPlaying = true
            notifyItemChanged(position) //recyclerview한테 알려주기
        }

        holder.btnPause.setOnClickListener {
            song.isPlaying = false
            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int = songList.size

}