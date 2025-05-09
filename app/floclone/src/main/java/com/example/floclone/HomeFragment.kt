package com.example.floclone

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.floclone.adaptor.AlbumRecyclerAdaptor
import com.example.floclone.adaptor.BannerAdaptor
import com.example.floclone.database.Album
import com.example.floclone.database.AlbumDatabase
import com.example.floclone.database.SongDatabase
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator3
import java.util.Timer
import java.util.TimerTask

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //ViewPager를 위한 어댑터 + Looper
    private lateinit var vpHome: ViewPager2
    private lateinit var cIndicatorHome: CircleIndicator3
    private lateinit var bannerAdapter: BannerAdaptor

    // Handler와 timer를 이용해서 시간 delay
    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //오늘 발매 음악 정리
        setAblumRecyclerView()

        //viewpager 설정
        //viewpager로 보여주기
        vpHome = view.findViewById(R.id.vp_Home)
        cIndicatorHome = view.findViewById(R.id.cindicator_home)
        val bannerList = listOf(
            Banner("달밤의 감성 산책", "#7DC1F6", "총 10곡 2025.03.30", Song("Lady", "Kenshi Yonezu", R.drawable.album_lady, "Lost Corner"),  Song("Spinning Globe", "Kenshi Yonezu", R.drawable.spinningglob, "Lost Corner")),
            Banner("요네즈 켄시 최신 특집", "#A67CF6", "총 23곡 2025.04.01", Song("BOW AND ARROW", "Kenshi Yonezu", R.drawable.bowandarrow, "digital single"), Song("Plazma", "Kenshi Yonezu", R.drawable.plazma, "digital single")),
            Banner("오늘의 추천 노래", "#009688", "총 17곡 2025.03.31", Song("Pop Song", "Kenshi Yonezu", R.drawable.yone_lostcorner, "Lost Corner"), Song("毎日", "Kenshi Yonezu", R.drawable.yone_lostcorner, "Lost Corner"))

        )
        bannerAdapter = BannerAdaptor(bannerList)
        vpHome.adapter = bannerAdapter
        cIndicatorHome.setViewPager(vpHome)
        startAutoBanner(bannerAdapter)

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    //일정 시간마다 run()을 수행
    private fun startAutoBanner(adaptor: BannerAdaptor) {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    val nextItem = vpHome.currentItem + 1
                    if (nextItem < adaptor.itemCount) {
                        vpHome.currentItem = nextItem
                    } else {
                        vpHome.currentItem = 0 // 마지막 페이지에서 첫 페이지로 순환
                    }
                }
            }
        }, 7000, 7000) //맨 처음 지연시간, 그 후 지연시간
    }

    //recyclerview 설정
    //:: 는 함수를 참조해서 넘긴다
    private fun setAblumRecyclerView(){
        //recylcerView를 통해 연결해보자
        val rcv_categorySong = view?.findViewById<RecyclerView>(R.id.rcv_categorySong_home)
        //사용할 Item들을 정의(AlbumList)
        val albumDao = AlbumDatabase.getDatabase(requireContext()).albumDao()

        lifecycleScope.launch{
            val albumList = albumDao.getAllAlbums()

            val adaptor_categorySong = AlbumRecyclerAdaptor(albumList, ::moveAlbumFragment, ::setMiniPlayerView)
            rcv_categorySong?.adapter = adaptor_categorySong
            //recyclerview에서 아이템 배치 방식을 나타내는 부분(수평) - 선형으로 수평 ->(false) 방향
            rcv_categorySong?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }


        val songList = listOf(
            Song("アイドル", "Yoasobi", R.drawable.thebook3, "THE BOOK 3"),
            Song("Lady", "Kenshi Yonezu", R.drawable.album_lady, "Lost Corner"),
            Song("愛を伝えたいだとか", "Aimyon", R.drawable.aiwotsutaetaidatoka, "愛を伝えたいだとか"),
            Song("勇者", "Yoasobi", R.drawable.thebook3, "THE BOOK 3"),
            Song("BOW AND ARROW", "Kenshi Yonezu", R.drawable.bowandarrow, "Digital single"),
            Song("群青", "Yoasobi", R.drawable.thebook, "THE BOOK"),
        )

    }

    fun setMiniPlayerView(album: Album){
        val homehome = requireActivity() as? MainActivity

        val songDao = SongDatabase.getDatabase(requireContext()).songDao()
        lifecycleScope.launch {
            val albumSongs = songDao.getSongsByAlbumIdx(album.id)
            homehome?.updateMiniplayerString(albumSongs.get(0).title, albumSongs.get(0).singer)
        }



        //직접 할 수도 있다.
        //homehome?.tvTitle.text = song.title
    }

    fun moveAlbumFragment(album: Album){
        //val action = HomeFragmentDirections.actionNavigationHomeFragmentToAlbumFragment(song)
        //findNavController().navigate(action)
        val songDao = SongDatabase.getDatabase(requireContext()).songDao()
        lifecycleScope.launch {
            val albumSongs = songDao.getSongsByAlbumIdx(album.id)

            // AlbumFragment의 인스턴스를 생성하고 Bundle로 인자 전달
            val albumFragment = AlbumFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("album", album)
                    putParcelableArrayList("songs", ArrayList(albumSongs))
                }
            }
            // fragment container (예: activity_main.xml에 있는 FrameLayout의 id를 fragment_container라 가정)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.ct_home, albumFragment)
                .addToBackStack(null)
                .commit()

        }

    }

}