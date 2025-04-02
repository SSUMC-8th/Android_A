package com.example.floclone

import android.os.Bundle
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
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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


        val song: Song? = arguments?.getParcelable<Song>("song")

        //val nowSong = args.song
        val nowSong = song
        albumImage = view.findViewById<ShapeableImageView>(R.id.imv_albumCover_albumFragment);

        view.findViewById<TextView>(R.id.tv_albumName_albumFragment).text = nowSong!!.albumName
        view.findViewById<TextView>(R.id.tv_artistName_albumFragment).text = nowSong.artist
        albumImage.setImageResource(nowSong.image)

        view.findViewById<ImageButton>(R.id.btn_arrowBck_albumFragment).setOnClickListener {
            //findNavController().popBackStack()
            requireActivity().supportFragmentManager.popBackStack()
        }

        //viewpager랑 tablayout을 연결
        val tabLayout = view.findViewById<TabLayout>(R.id.tbl_ablumInformation_albumFragment);
        val viewPager = view.findViewById<ViewPager2>(R.id.vp_showthree_albumFragment);
        viewPager.adapter = AlbumViewAdaptor(this, nowSong)

        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            tab.text = when(position) {
                0 -> "수록곡"
                1 -> "상세정보"
                2 -> "영상"
                else -> ""
            }
        }.attach()



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