package com.example.floclone.adaptor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.R
import com.example.floclone.Song

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Locker_SavesongFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Locker_SavesongFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_locker__savesong, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSavesongRecyclerView()

    }


    private fun setSavesongRecyclerView(){

        val rcv_savesong = view?.findViewById<RecyclerView>(R.id.rcv_savesongs_savesong)
        val songList = arrayListOf(
            Song("アイドル", "Yoasobi", R.drawable.thebook3, "THE BOOK 3"),
            Song("Lady", "Kenshi Yonezu", R.drawable.album_lady, "Lost Corner"),
            Song("Spinning Globe", "Kenshi Yonezu", R.drawable.spinningglob, "Lost Corner"),
            Song("勇者", "Yoasobi", R.drawable.thebook3, "THE BOOK 3"),
            Song("毎日", "Kenshi Yonezu", R.drawable.yone_lostcorner, "Lost Corner"),
            Song("青春と青春と青春", "Aimyon", R.drawable.kimiwarockwokikanai, "君はロックを聴かない"),
            Song("BOW AND ARROW", "Kenshi Yonezu", R.drawable.bowandarrow, "Digital single"),
            Song("マリーゴールド", "Aimyon", R.drawable.marigold, "マリーゴールド"),
            Song("夜に駆ける", "Yoasobi", R.drawable.thebook, "THE BOOK"),
            Song("群青", "Yoasobi", R.drawable.thebook, "THE BOOK"),
        )


        val adaptor_savesong = SavesongRecyclerAdaptor(songList)
        rcv_savesong?.adapter = adaptor_savesong
        //recyclerview에서 아이템 배치 방식을 나타내는 부분(수평) - 선형으로 수평 ->(false) 방향
        rcv_savesong?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Locker_SavesongFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Locker_SavesongFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}