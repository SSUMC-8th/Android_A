package com.example.umc_8th.flo_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_8th.R
import com.example.umc_8th.databinding.FragmentSavedSongBinding
import com.google.gson.Gson
//import umc.study.umc_8th.R
//import umc.study.umc_8th.databinding.FragmentSavedSongBinding

class SavedSongFragment:Fragment() {
    lateinit var binding:FragmentSavedSongBinding
    lateinit var songDB: SongDatabase
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
        albumDatas.apply {
            add(Album(id = 1, title = "LILAC", singer = "아이유 (IU)", coverImage = R.drawable.img_album_exp2))
            add(Album(id = 2, title = "Butter", singer = "BTS", coverImage = R.drawable.img_album_exp))
            add(Album(id = 3, title = "NextLevel", singer = "에스파", coverImage = R.drawable.img_album_exp3))
            add(Album(id = 4, title = "Weekend", singer = "태연", coverImage = R.drawable.img_album_exp4))
            add(Album(id = 5, title = "BBoom BBoom", singer = "모모랜드", coverImage = R.drawable.img_album_exp5))
            add(Album(id = 6, title = "Drama", singer = "에스타", coverImage = R.drawable.img_album_drama))
            add(Album(id = 1, title = "LILAC", singer = "아이유 (IU)", coverImage = R.drawable.img_album_exp2))
            add(Album(id = 2, title = "Butter", singer = "BTS", coverImage = R.drawable.img_album_exp))
            add(Album(id = 3, title = "NextLevel", singer = "에스파", coverImage = R.drawable.img_album_exp3))
            add(Album(id = 4, title = "Weekend", singer = "태연", coverImage = R.drawable.img_album_exp4))
            add(Album(id = 5, title = "BBoom BBoom", singer = "모모랜드", coverImage = R.drawable.img_album_exp5))
            add(Album(id = 6, title = "Drama", singer = "에스타", coverImage = R.drawable.img_album_drama))
        }
//        val lockerAlbumRVAdapter = LockerAlbumRVAdapter(albumDatas)
//        binding.lockerMusicAlbumRv.adapter = lockerAlbumRVAdapter
//        binding.lockerMusicAlbumRv.layoutManager = LinearLayoutManager(requireActivity())
//        lockerAlbumRVAdapter.setItemClickListener(object : LockerAlbumRVAdapter.OnItemClickListener {
//            override fun onItemClick(album: Album) {
//                changeAlbumFragment(album)
//            }
//
//            override fun onRemoveAlbum(position: Int) {
//                lockerAlbumRVAdapter.removeItem(position)
//            }
//        })
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        initRecyclerview()
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
}