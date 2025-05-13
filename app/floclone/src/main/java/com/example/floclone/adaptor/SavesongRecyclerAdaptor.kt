package com.example.floclone.adaptor

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
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
import com.example.floclone.database.SongDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.graphics.Color
import com.google.firebase.database.FirebaseDatabase

import com.example.floclone.database.Song as SongDB

class SavesongRecyclerAdaptor(
    private val songList:ArrayList<SongDB>, private val uid:String
    ): RecyclerView.Adapter<SavesongRecyclerAdaptor.ViewHolder>() {

    //lockerfragment 전용
    private var selectMode = false



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
        holder.imageView.setImageResource(song.coverImg ?: R.drawable.gibonsong)

        //lockerfragment에서 줄 시 색깔 변경
        holder.itemView.setBackgroundColor(
            if (selectMode){
                Color.parseColor("#EEEEEE")
            }
            else {
                Color.WHITE
            }
        )

        holder.titleView.text = song.title
        holder.artistView.text = song.singer

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

            //일단 값 변경 후, DB에 반영한다.
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION && pos < songList.size) {
                songList.get(pos).isLike = false
            }


            //그냥 FireStore에서 해당 아이템을 지우고 UI 반영하자.
            if(!uid.equals("")){
                val dbLike = FirebaseDatabase.getInstance().getReference("Like").child(uid)
                val nSong = songList.get(pos)
                val sid = "song_${nSong.id}"
                dbLike.child(sid).removeValue()
            }

            if (pos != RecyclerView.NO_POSITION && pos < songList.size) {
                songList.removeAt(pos)
                notifyItemRemoved(pos) //삭제 후 애니메이션 반영
                notifyItemRangeChanged(pos, songList.size - pos)
            }


            /*
            //sharedPreference에도 적용
            val sharedPref = holder.itemView.context.getSharedPreferences("Song", Context.MODE_PRIVATE)
            val gson = Gson()
            val getJson = sharedPref.getString("songList", "")
            val type = object : TypeToken<ArrayList<SongDB>>() {}.type
            val tmpSongList: ArrayList<SongDB> = gson.fromJson(getJson, type)

            for(i in 0 until tmpSongList.size){
                if(tmpSongList.get(i).id == songList.get(pos).id){
                    tmpSongList.get(i).isLike = false
                }
            }

            val jsonSongList = gson.toJson(tmpSongList)
            sharedPref.edit().putString("songList", jsonSongList).apply()
            */

            /*
            //일단 DB에는 비동기적으로 반영이 될 것이다.
            kotlinx.coroutines.GlobalScope.launch {
                SongDatabase.getDatabase(holder.itemView.context).songDao()
                    .updateSong(songList.get(pos))

                //UI 작업은 메인 쓰레드
                withContext(kotlinx.coroutines.Dispatchers.Main){
                    //그리고 recyclerview에서 UI에 반영하기
                    if (pos != RecyclerView.NO_POSITION && pos < songList.size) {
                        songList.removeAt(pos)
                        notifyItemRemoved(pos) //삭제 후 애니메이션 반영
                        notifyItemRangeChanged(pos, songList.size - pos)
                    }
                }
            }
            */

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



    //lockerfragment 전용
    fun doSelectMode(){
        selectMode = true
        notifyDataSetChanged()
    }

    fun disableSelectMode(){
        selectMode = false
        notifyDataSetChanged()
    }

    fun deleteAllItems(context: Context){

        //그냥 firebase 전체 날리기
        val dbLike = FirebaseDatabase.getInstance().getReference("Like").child(uid)
        dbLike.removeValue()

        songList.clear()
        notifyDataSetChanged()

        /*
        //1. 일단 DB에 반영
        val dao = SongDatabase.getDatabase(context).songDao()
        kotlinx.coroutines.GlobalScope.launch {
            for(song in songList){
                song.isLike = false
                dao.updateSong(song)
            }
            
            //2. sharedPreferences 업데이트
            val sharedPref = context.getSharedPreferences("Song", Context.MODE_PRIVATE)
            val gson = Gson()
            val getJson = sharedPref.getString("songList", "")
            val type = object : TypeToken<ArrayList<SongDB>>() {}.type
            val tmpSongList: ArrayList<SongDB> = gson.fromJson(getJson, type)

            for(i in 0 until tmpSongList.size){
                tmpSongList.get(i).isLike = false
            }

            val jsonSongList = gson.toJson(tmpSongList)
            sharedPref.edit().putString("songList", jsonSongList).apply()

            //3. UI 초기화
            withContext(kotlinx.coroutines.Dispatchers.Main){
                songList.clear()
                notifyDataSetChanged()
            }
        }
        */
    }
}