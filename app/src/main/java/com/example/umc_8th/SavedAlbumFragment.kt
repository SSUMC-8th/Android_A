package com.example.umc_8th

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import umc.study.umc_8th.databinding.FragmentSavedAlbumBinding

class SavedAlbumFragment : Fragment() {
    private lateinit var binding: FragmentSavedAlbumBinding
    private lateinit var adapter: SavedAlbumAdapter
    private val database = FirebaseDatabase.getInstance()
    private lateinit var userId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSavedAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = getUserIdFromSharedPrefs() // sharedPrefs에서 저장한 id 꺼내기

        adapter = SavedAlbumAdapter()
        binding.savedAlbumRecyclerView.adapter = adapter
        binding.savedAlbumRecyclerView.layoutManager = LinearLayoutManager(context)

        val likesRef = database.getReference("likes").child(userId)
        likesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val albumList = mutableListOf<Album>()
                for (albumSnapshot in snapshot.children) {
                    val album = albumSnapshot.getValue(Album::class.java)
                    album?.let { albumList.add(it) }
                }
                adapter.submitList(albumList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getUserIdFromSharedPrefs(): String {
        val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("id", "") ?: ""
    }
}
