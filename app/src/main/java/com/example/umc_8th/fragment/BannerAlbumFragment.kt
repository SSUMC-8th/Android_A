package com.example.umc_8th.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_8th.adapter.BannerAlbumAdapter
import com.example.umc_8th.BannerAlbumItem
import com.example.umc_8th.MainActivity_2nd
import com.example.umc_8th.R
import com.example.umc_8th.databinding.FragmentBannerAlbumBinding

class BannerAlbumFragment : Fragment() {

    private var _binding: FragmentBannerAlbumBinding? = null
    private val binding get() = _binding!!

    private lateinit var bannerAdapter: BannerAlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as MainActivity_2nd).findViewById<View>(R.id.miniPlayer)?.visibility = View.GONE
        (activity as MainActivity_2nd).findViewById<View>(R.id.my_btm_nav)?.visibility = View.GONE


        _binding = FragmentBannerAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toggleOff.setOnClickListener {
            // 현재 이미지가 toggle_off이면 toggle_on으로 변경
            val currentDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.btn_toggle_off)
            if (binding.toggleOff.drawable.constantState == currentDrawable?.constantState) {
                binding.toggleOff.setImageResource(R.drawable.btn_toggle_on)  // 토글 온 이미지로 변경
            } else {
                binding.toggleOff.setImageResource(R.drawable.btn_toggle_off)  // 토글 오프 이미지로 변경
            }
        }


        val bannerAlbumItems = listOf(
            BannerAlbumItem(R.drawable.img_album_drama, "Drama", "aespa", "겨울 감성 앨범"),
            BannerAlbumItem(R.drawable.img_album_drama, "Seven", "Jung Kook", "설렘 주의보 앨범"),
            BannerAlbumItem(R.drawable.img_album_drama, "Ditto", "New Jeans", "힐링 감성 앨범")
        )

        // 어댑터 연결
        bannerAdapter = BannerAlbumAdapter(bannerAlbumItems)
        binding.bannerAlbumRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            //binding.albumRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


            adapter = bannerAdapter
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as MainActivity_2nd).findViewById<View>(R.id.miniPlayer)?.visibility = View.VISIBLE
        (activity as MainActivity_2nd).findViewById<View>(R.id.my_btm_nav)?.visibility = View.VISIBLE

    }
}
