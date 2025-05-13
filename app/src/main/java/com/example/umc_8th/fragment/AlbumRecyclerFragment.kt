//오늘발매음악
package com.example.umc_8th.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_8th.adapter.AlbumAdapter
import com.example.umc_8th.R
import com.example.umc_8th.databinding.FragmentAlbumRecyclerBinding
import androidx.navigation.fragment.findNavController
import com.example.umc_8th.MainActivity_2nd
import com.example.umc_8th.database.SongDatabase
import com.example.umc_8th.entity.toAlbumItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumRecyclerFragment : Fragment() {

    private var _binding: FragmentAlbumRecyclerBinding? = null
    private val binding get() = _binding!!

    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = SongDatabase.getDatabase(requireContext())
        val albumDao = db.albumDao()

        CoroutineScope(Dispatchers.IO).launch {
            // DB에서 앨범 데이터 가져오기
            val albumEntities = albumDao.getAlbums()

            // AlbumEntity를 AlbumItem으로 변환
            val albumList = albumEntities.map { it.toAlbumItem() }

            // UI 갱신은 메인 스레드에서
            CoroutineScope(Dispatchers.Main).launch {
                // 어댑터 설정 + 클릭 이벤트 추가
                albumAdapter = AlbumAdapter(
                    albumList,
                    onPlayClick = { albumId, title, artist ->
                        val songDao = db.songDao()

                        CoroutineScope(Dispatchers.IO).launch {
                            val song = songDao.getFirstSongByAlbumId(albumId)

                            song?.let {
                                MusicPlayerState.setCurrentSongId(it.songId)

                                // 🔽 진행률을 0으로 초기화하고 스레드 재시작
                                MusicPlayerState.restartProgressThread()

                                // 메인 스레드에서 MiniPlayer UI 업데이트
                                CoroutineScope(Dispatchers.Main).launch {
                                    (requireActivity() as MainActivity_2nd).updateMiniPlayer(
                                        title = it.title,
                                        artist = it.singer,
                                        isPlaying = true
                                    )
                                }
                            }
                        }
                    },

//                    onPlayClick = { albumId, title, artist ->
//                        val songDao = db.songDao()
//
//                        CoroutineScope(Dispatchers.IO).launch {
//                            val song = songDao.getFirstSongByAlbumId(albumId)  // 바로 사용할 수 있음!
//
//                            song?.let {
//                                MusicPlayerState.setCurrentSongId(it.songId)
//
//                                CoroutineScope(Dispatchers.Main).launch {
//                                    (requireActivity() as MainActivity_2nd).updateMiniPlayer(
//                                        title = it.title,
//                                        artist = it.singer,
//                                        isPlaying = true
//                                    )
//                                }
//                            }
//                        }
//                    }

                    onItemClick = { album ->
                        val bundle = Bundle().apply {
                            putString("title", album.albumName)
                            putString("artist", album.artistName)
                            putInt("imageRes", album.albumImage)
                        }
                        findNavController().navigate(R.id.action_homeFragment_to_albumFragment, bundle)
                    }
                )


                // RecyclerView 설정
                binding.albumRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.albumRecyclerView.adapter = albumAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
