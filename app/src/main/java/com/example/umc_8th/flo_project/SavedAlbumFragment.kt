package com.example.umc_8th.flo_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_8th.R
import com.example.umc_8th.databinding.FragmentSavedAlbumBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson

class SavedAlbumFragment :Fragment() {
    lateinit var binding:FragmentSavedAlbumBinding
    lateinit var albumDB: SongDatabase
    private lateinit var database: DatabaseReference
    private var albumDatas= arrayListOf(
        Album(1, "LILAC", "아이유 (IU)", R.drawable.img_album_exp2),
        Album(2, "Butter", "BTS", R.drawable.img_album_exp),
        Album(3, "NextLevel", "에스파", R.drawable.img_album_exp3),
        Album(4, "Weekend", "태연", R.drawable.img_album_exp4),
        Album(5, "BBoom BBoom", "모모랜드", R.drawable.img_album_exp5),
        Album(6, "Drama", "에스타", R.drawable.img_album_drama),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedAlbumBinding.inflate(inflater, container, false)
        albumDB = SongDatabase.getInstance(requireContext())!!
        database = FirebaseDatabase.getInstance().reference
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadLikedAlbumsFromFirebase()
    }
    private fun loadLikedAlbumsFromFirebase() {
        database.child("likes").get().addOnSuccessListener { snapshot ->
            val likedIds = snapshot.children
                .filter { it.getValue(Boolean::class.java) == true }
                .mapNotNull { it.key?.toIntOrNull() }
                .toSet()

            val likedAlbums = albumDatas.filter { it.id in likedIds }

            initRecyclerView(ArrayList(likedAlbums))
        }.addOnFailureListener {
            Toast.makeText(context, "좋아요된 앨범을 불러오지 못했습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerView(filteredAlbums: ArrayList<Album>) {
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context)
        val albumRVAdapter = SavedAlbumRVAdapter()

        albumRVAdapter.setMyItemClickListener(object : SavedAlbumRVAdapter.MyItemClickListener {
            override fun onRemoveSong(albumId: Int) {
                database.child("likes").child(albumId.toString()).setValue(false)
                loadLikedAlbumsFromFirebase() // 새로고침
            }
        })

        binding.lockerSavedSongRecyclerView.adapter = albumRVAdapter
        albumRVAdapter.addAlbums(filteredAlbums)
    }

    private fun initRecyclerview(){
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val albumRVAdapter = SavedAlbumRVAdapter()
        //리스너 객체 생성 및 전달

        albumRVAdapter.setMyItemClickListener(object : SavedAlbumRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                albumDB.albumDao().getLikedAlbums(getJwt())
            }
        })

        binding.lockerSavedSongRecyclerView.adapter = albumRVAdapter

        albumRVAdapter.addAlbums(albumDB.albumDao().getLikedAlbums(getJwt()) as ArrayList)
    }

    private fun getJwt() : Int {
        val spf = activity?.getSharedPreferences("auth" , AppCompatActivity.MODE_PRIVATE)
        val jwt = spf!!.getInt("jwt", 0)
        return jwt
    }
}