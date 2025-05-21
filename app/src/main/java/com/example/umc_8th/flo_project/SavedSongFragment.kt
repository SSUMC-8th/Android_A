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
import com.example.umc_8th.databinding.FragmentSavedSongBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
//import umc.study.umc_8th.R
//import umc.study.umc_8th.databinding.FragmentSavedSongBinding

class SavedSongFragment:Fragment() {
    lateinit var binding:FragmentSavedSongBinding
    lateinit var songDB: SongDatabase
    private lateinit var database: DatabaseReference
    private var albumDatas = ArrayList<Album>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSavedSongBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!
        database = FirebaseDatabase.getInstance().reference
//        albumDatas.apply {
//            add(Album(id = 1, title = "LILAC", singer = "아이유 (IU)", coverImage = R.drawable.img_album_exp2))
//            add(Album(id = 2, title = "Butter", singer = "BTS", coverImage = R.drawable.img_album_exp))
//            add(Album(id = 3, title = "NextLevel", singer = "에스파", coverImage = R.drawable.img_album_exp3))
//            add(Album(id = 4, title = "Weekend", singer = "태연", coverImage = R.drawable.img_album_exp4))
//            add(Album(id = 5, title = "BBoom BBoom", singer = "모모랜드", coverImage = R.drawable.img_album_exp5))
//            add(Album(id = 6, title = "Drama", singer = "에스타", coverImage = R.drawable.img_album_drama))
//            add(Album(id = 1, title = "LILAC", singer = "아이유 (IU)", coverImage = R.drawable.img_album_exp2))
//            add(Album(id = 2, title = "Butter", singer = "BTS", coverImage = R.drawable.img_album_exp))
//            add(Album(id = 3, title = "NextLevel", singer = "에스파", coverImage = R.drawable.img_album_exp3))
//            add(Album(id = 4, title = "Weekend", singer = "태연", coverImage = R.drawable.img_album_exp4))
//            add(Album(id = 5, title = "BBoom BBoom", singer = "모모랜드", coverImage = R.drawable.img_album_exp5))
//            add(Album(id = 6, title = "Drama", singer = "에스타", coverImage = R.drawable.img_album_drama))
//        }
        albumDatas = arrayListOf(
            Album(1, "LILAC", "아이유 (IU)", R.drawable.img_album_exp2),
            Album(2, "Butter", "BTS", R.drawable.img_album_exp),
            Album(3, "NextLevel", "에스파", R.drawable.img_album_exp3),
            Album(4, "Weekend", "태연", R.drawable.img_album_exp4),
            Album(5, "BBoom BBoom", "모모랜드", R.drawable.img_album_exp5),
            Album(6, "Drama", "에스타", R.drawable.img_album_drama),
        )
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        loadLikedAlbumsFromFirebase()
//        initRecyclerview()
    }
    private fun loadLikedAlbumsFromFirebase() {
        database.child("likes").get().addOnSuccessListener { snapshot ->
            val likedIds = snapshot.children
                .filter { it.getValue(Boolean::class.java) == true }
                .mapNotNull { it.key?.toIntOrNull() }
                .toSet()

            val likedSongs = albumDatas
                .map { album ->
                    val isLiked = album.id in likedIds
                    Song(
                        title = album.title ?: "",
                        singer = album.singer ?: "",
                        second = 0,
                        playTime = 200,
                        isPlaying = false,
                        music = "sample", // 적절한 값
                        coverImg = album.coverImage,
                        isLike = isLiked,
                        albumIdx = album.id
                    ).apply {
                        id = album.id
                    }
                }
                .filter { it.isLike }

            initRecyclerview(ArrayList(likedSongs))
        }.addOnFailureListener {
            Toast.makeText(context, "좋아요 데이터를 불러오지 못했습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerview(filteredAlbums: ArrayList<Song>) {
        binding.lockerMusicAlbumRv.layoutManager = LinearLayoutManager(requireActivity())
        val lockerAlbumRVAdapter = LockerAlbumRVAdapter()

        lockerAlbumRVAdapter.setItemClickListener(object : LockerAlbumRVAdapter.OnItemClickListener {
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(songId: Int) {
                // 좋아요 해제 처리 (Firebase에서 삭제)
                database.child("likes").child(songId.toString()).setValue(false)
                loadLikedAlbumsFromFirebase() // 새로고침
            }
        })

        binding.lockerMusicAlbumRv.adapter = lockerAlbumRVAdapter
        lockerAlbumRVAdapter.addSongs(filteredAlbums)
    }

    private fun changeAlbumFragment(album: Album) {
        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumToJson = gson.toJson(album)
                    putString("album", albumToJson)
                }
            })
            .commitAllowingStateLoss()
    }

    private fun initRecyclerview(){
        binding.lockerMusicAlbumRv.layoutManager = LinearLayoutManager(requireActivity())
        val lockerAlbumRVAdapter = LockerAlbumRVAdapter()

        lockerAlbumRVAdapter.setItemClickListener(object : LockerAlbumRVAdapter.OnItemClickListener {
            override fun onItemClick(album: Album) {

            }

            override fun onRemoveAlbum(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }
        })
        binding.lockerMusicAlbumRv.adapter = lockerAlbumRVAdapter
        lockerAlbumRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
    }
}
