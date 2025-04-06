package com.example.flo

import com.example.flo.databinding.FragmentAlbumBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class albumFragment : Fragment(){
    lateinit var binding : FragmentAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        //binding.albumBackIv.setOnClickListner{
         //   (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, albumFragment()).commitAllowingStateLoss()
        //}


        return binding.root
    }
}