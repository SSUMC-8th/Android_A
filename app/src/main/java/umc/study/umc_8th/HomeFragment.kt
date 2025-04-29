package umc.study.umc_8th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var albumRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        albumRecyclerView = view.findViewById(R.id.rvAlbumList)

        val albumList = listOf(
            (R.drawable.img_album_exp),
            (R.drawable.img_album_exp2),
            (R.drawable.img_album_exp3),
            (R.drawable.img_album_exp4),
            (R.drawable.img_album_exp5)
        )

        albumAdapter = AlbumAdapter(albumList) { selectedAlbum ->

            val AlbumFragment = AlbumFragment().apply {
                arguments = Bundle().apply {
                    putInt("albumImage", selectedAlbum)
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, AlbumFragment)
                .addToBackStack(null)
                .commit()
        }

        albumRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        albumRecyclerView.adapter = albumAdapter

        return view
    }
}
