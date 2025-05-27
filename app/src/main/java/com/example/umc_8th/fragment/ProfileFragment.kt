package com.example.umc_8th.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.umc_8th.LoginActivity
import com.example.umc_8th.adapter.StorageAdapter
import com.example.umc_8th.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

//    private fun updateLoginButton() {
//        if (auth.currentUser == null) {
//            binding.loginBtn.text = "로그인"
//        } else {
//            binding.loginBtn.text = "로그아웃"
//        }
//    }

    //현재 로그인
    private fun isLoggedIn(): Boolean {
        val prefs = requireContext().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("isLoggedIn", false)

        // ✅ 로그 추가
        Log.d("AUTH_LOG", "ProfileFragment에서 로그인 상태 확인: $isLoggedIn")

        return isLoggedIn
    }


    //로그인 상태로 바꿈?
    private fun setLoggedIn(loggedIn: Boolean) {
        val prefs = requireContext().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        prefs.edit().putBoolean("isLoggedIn", loggedIn).apply()
    }

    private fun updateLoginButton() {
        binding.loginBtn.text = if (isLoggedIn()) "로그아웃" else "로그인"
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //updateLoginButton()
        binding.loginBtn.setOnClickListener {
            if (isLoggedIn()) {
                // 로그아웃
                setLoggedIn(false)
                val prefs = requireContext().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
                prefs.edit().clear().apply()

                Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                updateLoginButton()
            } else {
                // 로그인 화면으로 이동
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }


//        binding.loginBtn.setOnClickListener {
//            if (auth.currentUser == null) {
//                // 로그인 화면으로 이동
//                val intent = Intent(requireContext(), LoginActivity::class.java)
//                startActivity(intent)
//
//            } else {
//                // 로그아웃 처리
//                auth.signOut()
//                Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
//                updateLoginButton()
//            }
//        }

        // ViewPager2 어댑터 설정
        val adapter = StorageAdapter(this)
        binding.pager.adapter = adapter

        // TabLayout과 ViewPager2 연결
        val tabTitles = arrayOf("저장한 곡", "음악 파일", "저장 앨범")

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        updateLoginButton() // 프래그먼트가 다시 보일 때 버튼 텍스트 갱신
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
