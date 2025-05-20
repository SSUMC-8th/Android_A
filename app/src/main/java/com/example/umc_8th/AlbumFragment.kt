package com.example.umc_8th

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val albumTitle = arguments?.getString("albumTitle")
        val albumArtist = arguments?.getString("albumArtist")
        val albumCover = arguments?.getInt("albumCover")
        val albumIndex = arguments?.getInt("albumListIndex")
        Log.d("AlbumFragment", "$albumIndex")

        val titleTextView: TextView = view.findViewById(R.id.album_detail_title)
        val artistTextView: TextView = view.findViewById(R.id.album_detail_artist)
        val coverImageView: ImageView = view.findViewById(R.id.album_detail_cover)

        titleTextView.text = albumTitle
        artistTextView.text = albumArtist
        coverImageView.setImageResource(albumCover ?: R.drawable.img_album_exp2)

        val adapter = AlbumPagerAdapter(this, albumIndex)
        binding = FragmentAlbumBinding.bind(view)
        binding.viewPager.adapter = adapter
        auth = FirebaseAuth.getInstance()

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "수록곡"
                1 -> tab.text = "상세정보"
                2 -> tab.text = "영상"
            }
        }.attach()

        val user = auth.currentUser ?: return
        val userUid = user.uid

        val heartImageView: ImageView = view.findViewById(R.id.album_heart_iv)

        // 좋아요 상태 초기 설정
        database.child("likes").child(userUid).child(albumIndex.toString()).get()
            .addOnSuccessListener {
                val isLiked = it.getValue(Boolean::class.java) == true
                updateHeartIcon(heartImageView, isLiked)

                // 클릭 리스너 설정
                heartImageView.setOnClickListener {
                    Log.d("AlbumFragment", "Heart icon clicked")
                    val newLikeState = !isLiked
                    updateHeartIcon(heartImageView, newLikeState)
                    if (newLikeState) {
                        // 좋아요 추가
                        database.child("likes").child(userUid).child(albumIndex.toString()).setValue(true)
                    } else {
                        // 좋아요 제거
                        database.child("likes").child(userUid).child(albumIndex.toString()).removeValue()
                    }
                }
            }

        // 뒤로 가기 버튼 설정
        val backButton: ImageView = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun updateHeartIcon(imageView: ImageView, isLiked: Boolean) {
        val resId = if (isLiked) R.drawable.ic_my_like_on else R.drawable.ic_my_like_off
        imageView.setImageResource(resId)
    }



    }
