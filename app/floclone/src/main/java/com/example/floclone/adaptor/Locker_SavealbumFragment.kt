package com.example.floclone.adaptor

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.MainActivity
import com.example.floclone.R
import com.example.floclone.database.Album
import com.example.floclone.database.Song
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Locker_SavealbumFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Locker_SavealbumFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var btnChooseall : ConstraintLayout
    private lateinit var btnChooseallno : ConstraintLayout
    private lateinit var btnDeleteAll : ConstraintLayout
    private lateinit var adaptor_saveAlbum : SaveAlbumRecyclerAdaptor

    private var likeAlbumList: ArrayList<Album> = ArrayList()


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
        return inflater.inflate(R.layout.fragment_locker__savealbum, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnChooseall = view.findViewById<ConstraintLayout>(R.id.btn_chooseall_savealbum)
        btnChooseallno = view.findViewById<ConstraintLayout>(R.id.btn_chooseall_delete_savealbum)

        //얘는 MainActivity꺼다
        btnDeleteAll = requireActivity().findViewById<ConstraintLayout>(R.id.btn_delete_edit_bottomsheet)

        getLikedAlbumFromFirestore()


        btnChooseall.setOnClickListener {
            btnChooseall.visibility = TextView.INVISIBLE
            btnChooseallno.visibility = TextView.VISIBLE
            (requireActivity() as? MainActivity)?.showBottomActionBar()
            adaptor_saveAlbum.doSelectMode()
        }

        btnChooseallno.setOnClickListener {
            btnChooseall.visibility = TextView.VISIBLE
            btnChooseallno.visibility = TextView.INVISIBLE
            (requireActivity() as? MainActivity)?.hideBottomActionBar()
            adaptor_saveAlbum.disableSelectMode()
        }

        btnDeleteAll.setOnClickListener {
            adaptor_saveAlbum.deleteAllItems(requireContext())
            btnChooseallno.performClick() //버튼 누르기 코드적 실행
        }

    }

    private fun setAlbumRecyclerView(){
        val sharedPrefLogin = requireActivity().getSharedPreferences("login", MODE_PRIVATE)
        val uid = sharedPrefLogin.getString("id", null) ?: ""

        val rcv_savealbum = view?.findViewById<RecyclerView>(R.id.rcv_savealbums_savealbum)
        adaptor_saveAlbum = SaveAlbumRecyclerAdaptor(likeAlbumList, uid)
        rcv_savealbum?.adapter = adaptor_saveAlbum

        rcv_savealbum?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }


    private fun getLikedAlbumFromFirestore(){
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
                setAlbumRecyclerView()
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
         * @return A new instance of fragment Locker_SavealbumFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Locker_SavealbumFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}