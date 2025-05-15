package com.example.floclone.adaptor

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.MainActivity
import com.example.floclone.R
import com.example.floclone.Song
import com.example.floclone.database.SongDatabase
import com.google.firebase.database.FirebaseDatabase
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
    private lateinit var btnChooseallno : ConstraintLayout
    private lateinit var btnDeleteAll : ConstraintLayout
    private lateinit var adaptor_savesong : SavesongRecyclerAdaptor

    private var likeSongList: ArrayList<SongDB> = ArrayList()



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
        btnChooseallno = view.findViewById<ConstraintLayout>(R.id.btn_chooseall_delete_savesong)
        
        //얘는 MainActivity꺼다
        btnDeleteAll = requireActivity().findViewById<ConstraintLayout>(R.id.btn_delete_edit_bottomsheet)

        getLikedSongFromFirestore()

        btnChooseall.setOnClickListener {
            btnChooseall.visibility = TextView.INVISIBLE
            btnChooseallno.visibility = TextView.VISIBLE
            (requireActivity() as? MainActivity)?.showBottomActionBar()
            adaptor_savesong.doSelectMode()
        }

        btnChooseallno.setOnClickListener {
            btnChooseall.visibility = TextView.VISIBLE
            btnChooseallno.visibility = TextView.INVISIBLE
            (requireActivity() as? MainActivity)?.hideBottomActionBar()
            adaptor_savesong.disableSelectMode()
        }

        btnDeleteAll.setOnClickListener {
            adaptor_savesong.deleteAllItems(requireContext())
            btnChooseallno.performClick() //버튼 누르기 코드적 실행
        }

    }


    private fun setSavesongRecyclerView(){

        val sharedPrefLogin = requireActivity().getSharedPreferences("login", MODE_PRIVATE)
        val uid = sharedPrefLogin.getString("id", null) ?: ""

        val rcv_savesong = view?.findViewById<RecyclerView>(R.id.rcv_savesongs_savesong)

        //val dao = SongDatabase.getDatabase(requireContext()).songDao()

        lifecycleScope.launch {
            //var tmpsongList = dao.getLikedSongs()
            //var songList = ArrayList(tmpsongList)
            adaptor_savesong = SavesongRecyclerAdaptor(likeSongList, uid)
            rcv_savesong?.adapter = adaptor_savesong
            //recyclerview에서 아이템 배치 방식을 나타내는 부분(수평) - 선형으로 수평 ->(false) 방향
            rcv_savesong?.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }


    }

    //firestore에서 가져오기
    private fun getLikedSongFromFirestore(){
        //Firebase에서 유저 별로 isLike노래들 가져오기
        val sharedPrefLogin = requireActivity().getSharedPreferences("login", MODE_PRIVATE)
        val loginCheck = sharedPrefLogin.getBoolean("loginCheck", false)
        val uid = sharedPrefLogin.getString("id", null)

        if(loginCheck && uid != null){
            val dbLike = FirebaseDatabase.getInstance().getReference("Like").child(uid)
            dbLike.get().addOnSuccessListener { snapshot ->
                //uid가 가진 key들을 얻는다.
                for (child in snapshot.children) {
                    Log.d("tagcheck", "일단 1차: ${child.value}")
                    val map = child.value as? Map<String, Any>
                    map?.let {
                        Log.d("tagcheck", "map? 됬나?")
                        val song = SongDB(
                            id = (it["id"] as Long).toInt(),
                            title = it["title"] as String,
                            singer = it["singer"] as String,
                            second = (it["second"] as Long).toInt(),
                            playTime = (it["playTime"] as Long).toInt(),
                            isPlaying = it["playing"] as Boolean,
                            music = it["music"] as String,
                            coverImg = (it["coverImg"] as Long).toInt(),
                            isLike = it["like"] as Boolean,
                            albumIdx = (it["albumIdx"] as Long).toInt()
                        )
                        likeSongList.add(song)
                    }
                }
                //recylcerview update
                setSavesongRecyclerView()
            }
                .addOnFailureListener {
                    //
                }
        }
    }

    //로그인/로그아웃 시 UI 변경
    fun refreshUI(){
        getLikedSongFromFirestore()
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