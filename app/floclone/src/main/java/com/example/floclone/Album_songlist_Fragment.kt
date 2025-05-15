package com.example.floclone

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.adaptor.SongsRecyclerAdaptor
import com.example.floclone.database.Album
import com.google.android.material.imageview.ShapeableImageView
import com.example.floclone.database.Song as SongDB

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Album_songlist_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Album_songlist_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //인자
    private var song: Song? = null
    private lateinit var albumImage: ImageView

    private lateinit var album: Album
    private lateinit var songList: List<SongDB>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_songlist_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            album = it.getParcelable("album")!!
            songList = it.getParcelableArrayList("songs")!!
        }
        //albumImage = view.findViewById<ShapeableImageView>(R.id.imv_albumCover_albumFragment)

        //recyclerview 연결
        setSongsRecyclerView()

        setToggleButton()
    }

    fun setToggleButton(){
        val btnToggleoff = view?.findViewById<ImageButton>(R.id.btn_toggleoff_albumFragment)
        val btnToggleon = view?.findViewById<ImageButton>(R.id.btn_toggleon_albumFragment)

        //섞어
        btnToggleoff?.setOnClickListener {
            btnToggleoff?.visibility = View.GONE
            btnToggleon?.visibility = View.VISIBLE

            //섞어서 새로 연결
            val shuffle = songList.shuffled()
            val rcv_categorySong = view?.findViewById<RecyclerView>(R.id.rcv_menusongs_albumFragment)
            val adaptor_categorySong = SongsRecyclerAdaptor(shuffle)
            rcv_categorySong?.adapter = adaptor_categorySong
            rcv_categorySong?.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            
        }
        //원본
        btnToggleon?.setOnClickListener {
            btnToggleoff?.visibility = View.VISIBLE
            btnToggleon?.visibility = View.GONE

            //원본 보여줘
            setSongsRecyclerView()
        }

    }

    fun setSongsRecyclerView(){
        //recylcerView를 통해 연결해보자
        val rcv_categorySong = view?.findViewById<RecyclerView>(R.id.rcv_menusongs_albumFragment)
        //사용할 Item들을 정의

        val adaptor_categorySong = SongsRecyclerAdaptor(songList)
        rcv_categorySong?.adapter = adaptor_categorySong
        //recyclerview에서 아이템 배치 방식을 나타내는 부분(수평) - 선형으로 수평 ->(false) 방향
        rcv_categorySong?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Album_songlist_Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Album_songlist_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}