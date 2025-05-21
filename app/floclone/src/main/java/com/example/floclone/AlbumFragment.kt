package com.example.floclone

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.floclone.adaptor.AlbumViewAdaptor
import com.example.floclone.adaptor.SongsRecyclerAdaptor
import com.example.floclone.database.Album
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.FirebaseDatabase

import com.example.floclone.database.Song as SongDB

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //fragment -> fragment로 받은 내용
    private val args: AlbumFragmentArgs by navArgs()

    private lateinit var albumImage: ImageView;
    private lateinit var btnFavorite : ImageButton


    private lateinit var album: Album
    private lateinit var songList: List<SongDB>
    private lateinit var likeAlbumList : ArrayList<Album>
    private var albumLike : Boolean = false

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
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        likeAlbumList = ArrayList()

        arguments?.let {
            album = it.getParcelable("album")!!
            songList = it.getParcelableArrayList("songs")!!
        }

        //firestore에서 like 앨범 가져오기
        getAlbumFromFirestore()


        //val nowSong = args.song
        albumImage = view.findViewById<ShapeableImageView>(R.id.imv_albumCover_albumFragment);
        btnFavorite = view.findViewById<ImageButton>(R.id.btn_favorite_albumFragment)

        view.findViewById<TextView>(R.id.tv_albumName_albumFragment).text = album.title
        view.findViewById<TextView>(R.id.tv_artistName_albumFragment).text = album.singer
        albumImage.setImageResource(album.coverImg ?: R.drawable.gibonsong)

        view.findViewById<ImageButton>(R.id.btn_arrowBck_albumFragment).setOnClickListener {
            //findNavController().popBackStack()
            requireActivity().supportFragmentManager.popBackStack()
        }

        //viewpager랑 tablayout을 연결
        val tabLayout = view.findViewById<TabLayout>(R.id.tbl_ablumInformation_albumFragment);
        val viewPager = view.findViewById<ViewPager2>(R.id.vp_showthree_albumFragment);
        viewPager.adapter = AlbumViewAdaptor(this, album, songList)

        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            tab.text = when(position) {
                0 -> "수록곡"
                1 -> "상세정보"
                2 -> "영상"
                else -> ""
            }
        }.attach()

        btnFavorite.setOnClickListener { handleFavorite() }



    }

    private fun getAlbumFromFirestore(){
        val sharedPrefLogin = requireActivity().getSharedPreferences("login", MODE_PRIVATE)
        val loginCheck = sharedPrefLogin.getBoolean("loginCheck", false)
        val uid = sharedPrefLogin.getString("id", null)

        if(loginCheck && uid != null){
            val dbLike = FirebaseDatabase.getInstance().getReference("Like").child(uid).child("album")
            dbLike.get().addOnSuccessListener { snapshot ->
                for (child in snapshot.children) {
                    Log.d("tagcheck", "일단 1차: ${child.value}")
                    val map = child.value as? Map<String, Any>
                    map?.let {
                        Log.d("tagcheck", "map? 됬나?")
                        val album = Album(
                            id = (it["id"] as Long).toInt(),
                            title = it["title"] as String,
                            singer = it["singer"] as String,
                            coverImg = (it["coverImg"] as Long).toInt(),
                        )
                        likeAlbumList.add(album)
                    }
                }
                //UI 작업
                val aid = "album_${album.id}"
                for(al in likeAlbumList){
                    if("album_${al.id}".equals(aid)){
                        btnFavorite.setImageResource(R.drawable.ic_my_like_on)
                        albumLike = true
                    }
                }
            }
        }
    }

    private fun handleFavorite(){
        val sharedPrefLogin = requireActivity().getSharedPreferences("login", MODE_PRIVATE)
        val loginCheck = sharedPrefLogin.getBoolean("loginCheck", false)
        val uid = sharedPrefLogin.getString("id", null)

        if(loginCheck && uid != null){
            val dbLike = FirebaseDatabase.getInstance().getReference("Like").child(uid).child("album")
            if(albumLike){
                albumLike = false
                val aid = "album_${album.id}"
                dbLike.child(aid).removeValue()
                btnFavorite.setImageResource(R.drawable.ic_my_like_off)
            }
            else{
                albumLike = true
                val aid = "album_${album.id}"
                dbLike.child(aid).setValue(album)
                btnFavorite.setImageResource(R.drawable.ic_my_like_on)
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlbumFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlbumFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}