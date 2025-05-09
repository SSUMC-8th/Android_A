package com.example.floclone.adaptor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.R
import com.example.floclone.Song
import com.example.floclone.database.SongDatabase
import kotlinx.coroutines.launch

import com.example.floclone.database.Song as SongDB

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

    private lateinit var btnChooseall : ConstraintLayout
    private lateinit var btnChooseallDelete : ConstraintLayout

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
        btnChooseall = view.findViewById<ConstraintLayout>(R.id.btn_chooseall_savesong)
        btnChooseallDelete = view.findViewById<ConstraintLayout>(R.id.btn_chooseall_delete_savesong)

        setSavesongRecyclerView()

    }


    private fun setSavesongRecyclerView(){

        val rcv_savesong = view?.findViewById<RecyclerView>(R.id.rcv_savesongs_savesong)

        val dao = SongDatabase.getDatabase(requireContext()).songDao()

        lifecycleScope.launch {
            var tmpsongList = dao.getLikedSongs()
            var songList = ArrayList(tmpsongList)
            val adaptor_savesong = SavesongRecyclerAdaptor(songList)
            rcv_savesong?.adapter = adaptor_savesong
            //recyclerview에서 아이템 배치 방식을 나타내는 부분(수평) - 선형으로 수평 ->(false) 방향
            rcv_savesong?.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }


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