package com.example.umc_8th.flo_project

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.umc_8th.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator
//import umc.study.umc_8th.R
//import umc.study.umc_8th.databinding.FragmentLockerBinding

class LockerFragment : Fragment(){
    private lateinit var binding: FragmentLockerBinding
    private lateinit var lockerAdapter: LockerPageAdapter
    private val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
//        lockerAdapter = LockerPageAdapter(this)
//        binding.lockerContentVp.adapter = lockerAdapter

//        val lockerPageAdapter = LockerPageAdapter(this)
//        binding.lockerContentVp.adapter=lockerPageAdapter
//        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
//            tab.text = information[position]
//        }.attach()
        val lockerVPAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerVPAdapter

        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        val bottomSheetFragment = BottomSheetFragment()

        binding.lockerSelectAllTv.setOnClickListener {
            bottomSheetFragment.show(requireFragmentManager(), "BottomSheetDialog")
        }
        binding.lockerLoginTv.setOnClickListener{
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        initViews()
    }
//    private fun getJwt() : Int {
//        val spf = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
//        return spf!!.getInt("jwt", 0)
//    }
    private fun getMemberId(): Int {
        return requireContext()
            .getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getInt("memberId", -1)  // 기본값을 -1 로
    }

    private fun getJwt(): String? {
        val spf = requireContext()
            .getSharedPreferences("auth", Context.MODE_PRIVATE)
        // → getInt 대신 getString 사용
        return spf.getString("jwt", null)
    }

//    private fun initViews() {
//        val jwt : Int = getJwt()
//        if (jwt == 0) {
//            binding.lockerLoginTv.text="로그인"
//            binding.lockerLoginTv.setOnClickListener {
//                startActivity(Intent(requireActivity(), LoginActivity::class.java))
//            }
//        }
//
//        else {
//            binding.lockerLoginTv.text = "로그아웃"
//            binding.lockerLoginTv.setOnClickListener {
//                logout()
//                startActivity(Intent(requireActivity(), FloMainActivity::class.java))
//            }
//        }
//    }
private fun initViews() {
    val memberId = getMemberId()
    if (memberId < 0) {
        // 로그인 안 된 상태
        binding.lockerLoginTv.text = "로그인"
        binding.lockerLoginTv.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
    } else {
        // 로그인 된 상태
        binding.lockerLoginTv.text = "로그아웃"
        binding.lockerLoginTv.setOnClickListener {
            logout()
            startActivity(Intent(requireActivity(), FloMainActivity::class.java))
        }
    }
}

    private fun logout() {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()
        editor.remove("jwt")
        editor.apply()
    }
}