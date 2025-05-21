package com.example.floclone.adaptor

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.R
import com.example.floclone.database.Album
import com.google.firebase.database.FirebaseDatabase

class SaveAlbumRecyclerAdaptor(
    private val albumList:ArrayList<Album>, private val uid:String
): RecyclerView.Adapter<SaveAlbumRecyclerAdaptor.ViewHolder>() {

    //lockerfragment 전용
    private var selectMode = false

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.imv_songImage_savealbum_recycler)
        val titleView: TextView = itemView.findViewById(R.id.tv_songName_savealbum_recycler)
        val artistView: TextView = itemView.findViewById(R.id.tv_artistName_savealbum_recycler)
        val btnPlay: ImageButton = itemView.findViewById(R.id.btn_play_savealbum_recycler)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete_savealbum_recycler)
        val btnPause: ImageButton = itemView.findViewById(R.id.btn_pause_savealbum_recycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lockerfragment_savealbum_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albumList[position]
        holder.imageView.setImageResource(album.coverImg ?: R.drawable.gibonsong)

        //전체 선택 시 색깔 변경
        holder.itemView.setBackgroundColor(
            if (selectMode){
                Color.parseColor("#EEEEEE")
            }
            else {
                Color.WHITE
            }
        )

        //UI 연결
        holder.titleView.text = album.title
        holder.artistView.text = album.singer

        holder.btnDelete.setOnClickListener {

            //일단 처리할 위치 pos 값 get
            val pos = holder.bindingAdapterPosition

            //Firestore에 반영
            if(!uid.equals("")){
                val dbLike = FirebaseDatabase.getInstance().getReference("Like").child(uid).child("album")
                val nAlbum = albumList.get(pos)
                val aid = "album_${nAlbum.id}"
                dbLike.child(aid).removeValue()
            }

            if (pos != RecyclerView.NO_POSITION && pos < albumList.size) {
                albumList.removeAt(pos)
                notifyItemRemoved(pos) //삭제 후 애니메이션 반영
                notifyItemRangeChanged(pos, albumList.size - pos)
            }

        }

        
    }

    override fun getItemCount(): Int = albumList.size

    //lockerfragment 전용
    fun doSelectMode(){
        selectMode = true
        notifyDataSetChanged()
    }

    fun disableSelectMode(){
        selectMode = false
        notifyDataSetChanged()
    }

    fun deleteAllItems(context: Context) {

        //그냥 firebase 전체 날리기
        val dbLike = FirebaseDatabase.getInstance().getReference("Like").child(uid).child("album")
        dbLike.removeValue()

        albumList.clear()
        notifyDataSetChanged()
    }


}