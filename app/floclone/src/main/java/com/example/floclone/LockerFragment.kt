package com.example.floclone

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.floclone.adaptor.LockerViewAdaptor
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LockerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LockerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var btnLogin : Button
    private lateinit var btnLogout : Button


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
        return inflater.inflate(R.layout.fragment_locker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.tbl_storelay_lockerFragment);
        val viewPager = view.findViewById<ViewPager2>(R.id.vp_Locker_lockerFragment);

        //이 둘을 연결
        viewPager.adapter = LockerViewAdaptor(this)

        //TablayoutMediator는 tablayout과 viewpager를 연결
        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            tab.text = when(position) {
                0 -> "저장한 곡"
                1 -> "음악파일"
                else -> ""
            }
        }.attach()

        btnLogin = view.findViewById<Button>(R.id.btn_login_lockerFragment)
        btnLogout = view.findViewById<Button>(R.id.btn_logout_lockerFragment)

        val prefs = requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        val loginCheck = prefs.getBoolean("loginCheck", false)

        if(loginCheck){
            btnLogin.visibility = TextView.GONE
            btnLogout.visibility = TextView.VISIBLE
        }
        else{
            btnLogin.visibility = TextView.VISIBLE
            btnLogout.visibility = TextView.GONE
        }

        btnLogin.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
        btnLogout.setOnClickListener {
            logout()
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LockerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LockerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun logout(){
        //버튼 다시 보이게 하고
        btnLogin.visibility = TextView.VISIBLE
        btnLogout.visibility = TextView.GONE

        //shared 적용
        val sharedPref = requireActivity().getSharedPreferences("login", MODE_PRIVATE)

        sharedPref.edit()
            .putBoolean("loginCheck", false)
            .putString("id", "")
            .apply()


    }

    override fun onResume() {
        super.onResume()

        val prefs = requireActivity().getSharedPreferences("login", MODE_PRIVATE)
        val loginCheck = prefs.getBoolean("loginCheck", false)
        val uid = prefs.getString("id", null)

        //로그인이면
        if(loginCheck && uid != null){
            btnLogin.visibility = TextView.GONE
            btnLogout.visibility = TextView.VISIBLE
        }
        else{
            btnLogin.visibility = TextView.VISIBLE
            btnLogout.visibility = TextView.GONE

        }

    }
}