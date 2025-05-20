
package com.example.umc_8th

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var binding: FragmentHomeBinding

//    private val albumList = listOf(
//        Album("LILAC", "아이유 (IU)", R.drawable.img_album_exp2),
//        Album("Title 2", "Artist 2", R.drawable.img_album_exp4),
//        Album("Title 3", "Artist 3", R.drawable.img_album_exp3)
//    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 배너 이미지 목록
        val adbannerlist = listOf(
            R.drawable.img_home_viewpager_exp,
            R.drawable.img_home_viewpager_exp2,
        )

        val topbannerlist = listOf(
            R.drawable.img_first_album_default,
            R.drawable.img_potcast_exp,
            R.drawable.discovery_banner_aos
        )

        // ViewPager2 어댑터 설정
        val adAdapter = AdBannerAdapter(adbannerlist)
        binding = FragmentHomeBinding.bind(view)
        binding.adBannerVp.adapter = adAdapter

        val topAdapter = TopBannerAdapter(topbannerlist)
        binding.topBannerVp.adapter = topAdapter

        val tophandler = Handler(Looper.getMainLooper())
        val toprunnable = object : Runnable {
            override fun run() {
                val topcurrentItem = binding.topBannerVp.currentItem
                val nextItem = (topcurrentItem + 1) % topbannerlist.size
                binding.topBannerVp.setCurrentItem(nextItem, true)
                tophandler.postDelayed(this, 3000)
            }
        }
        tophandler.postDelayed(toprunnable, 3000)

        recyclerView = view.findViewById(R.id.home_today_music_album_rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        albumAdapter = AlbumAdapter(
            albumList = albumList,
            onAlbumClick = { album ->
                // 앨범 전체를 클릭했을 때: 앨범 상세 화면으로 이동
                openAlbumFragment(album)
            },
            onPlayClick = onPlayClick@{ album ->
                val track = album.trackList.firstOrNull()
                if (track != null) {
                    Log.d("HomeFragment", "클릭된 트랙: ${track.title}")

                    var mediaPlayer: MediaPlayer? = null
                    val duration: Int
                    try {
                        mediaPlayer = MediaPlayer.create(requireContext(), track.musicResId)
                        duration = mediaPlayer.duration
                        Log.d("HomeFragment", "트랙 재생 시간: $duration ms")
                    } catch (e: Exception) {
                        Log.e("HomeFragment", "MediaPlayer 생성 오류: ${e.message}", e)
                        return@onPlayClick // MediaPlayer 생성 실패 시 함수 종료
                    } finally {
                        mediaPlayer?.release() // 재생 시간을 얻은 후 MediaPlayer 즉시 해제
                    }

                    // 브로드캐스트로 MainActivity에 전송
                    val intent = Intent("com.example.umc_8th.ALBUM_PLAY").apply {
                        putExtra("title", track.title)
                        putExtra("artist", track.artist)
                        putExtra("progress", 0)
                        putExtra("duration", duration)
                    }
                    requireContext().sendBroadcast(intent)
                    Log.d("HomeFragment", "브로드캐스트 전송 완료: ${track.title}")


                    // 수록곡들을 Room DB에 저장 (백그라운드 스레드)
                    Thread {
                        val db = SongDatabase.getInstance(requireContext())
                        val songDao = db.songDao()

                        if (songDao == null) {
                            Log.e("HomeFragment", "SongDao가 null입니다. 데이터베이스가 올바르게 초기화되지 않았나요?")
                            return@Thread
                        }

                        try {
                            for (t in album.trackList) {
                                var currentTrackDuration = 0
                                try {
                                    val tempPlayer = MediaPlayer.create(requireContext(), t.musicResId)
                                    currentTrackDuration = tempPlayer.duration
                                    tempPlayer.release()
                                } catch (e: Exception) {
                                    Log.e("HomeFragment", "${t.title} 재생 시간 가져오기 오류: ${e.message}")
                                    currentTrackDuration = 240000 // 기본 4분
                                }

                                val song = Song(
                                    title = t.title,
                                    singer = t.artist,
                                    second = 0,
                                    playTime = currentTrackDuration,
                                    isPlaying = false,
                                    music = t.musicResId.toString(),
                                    coverImg = album.coverImage,
                                    isLike = false
                                )
                                songDao.insert(song)
                                Log.d("HomeFragment", "DB에 노래 삽입 완료: ${song.title}")

                                // **새로 추가된 로그: 각 곡 삽입 후 현재 DB 상태 확인**
                                val currentSongsInDb = songDao.getAllSongs()
                                Log.d("HomeFragment", "현재 DB에 있는 곡들 (${currentSongsInDb.size}개):")
                                currentSongsInDb.forEachIndexed { index, s ->
                                    Log.d("HomeFragment", "  ${index + 1}. ID: ${s.id}, 제목: ${s.title}, 가수: ${s.singer}")
                                }
                            }

                            // 가장 첫 번째 곡을 현재 곡으로 저장
                            val firstInserted = songDao.getAllSongs().lastOrNull()
                            firstInserted?.let {
                                val prefs = requireContext().getSharedPreferences("song_prefs", Context.MODE_PRIVATE)
                                prefs.edit().putInt("songId", it.id).apply()
                                Log.d("HomeFragment", "SharedPreferences에 songId 저장 완료: ${it.id}")
                            } ?: Log.w("HomeFragment", "삽입된 노래가 없어서 현재 곡 ID를 저장할 수 없습니다.")

                            // **새로 추가된 로그: 모든 곡 삽입 후 최종 DB 상태 확인**
                            val finalSongsInDb = songDao.getAllSongs()
                            Log.d("HomeFragment", "모든 곡 삽입 후 최종 DB에 있는 곡들 (${finalSongsInDb.size}개):")
                            finalSongsInDb.forEachIndexed { index, s ->
                                Log.d("HomeFragment", "  ${index + 1}. ID: ${s.id}, 제목: ${s.title}, 가수: ${s.singer}")
                            }

                        } catch (e: Exception) {
                            Log.e("HomeFragment", "데이터베이스 삽입 중 오류 발생: ${e.message}", e)
                        }
                    }.start()
                } else {
                    Log.w("HomeFragment", "앨범에 트랙이 없습니다: ${album.title}")
                }
            }

        )
        recyclerView.adapter = albumAdapter
    }

    private fun openAlbumFragment(album: Album) {
        val bundle = Bundle().apply {
            putString("albumTitle", album.title)
            putString("albumArtist", album.artist)
            putInt("albumCover", album.coverImage)
            putInt("albumListIndex", albumList.indexOf(album))
        }

        findNavController().navigate(R.id.action_homeFragment_to_albumFragment, bundle)
    }
}