package com.example.umc_8th

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_8th.databinding.ActivityMain2ndBinding


class MainActivity_2nd : AppCompatActivity(){

    private lateinit var mBinding : ActivityMain2ndBinding
    val TAG: String = "로그"

//    val albumList = listOf(
//        AlbumModel(R.drawable.img_album_exp, "Butter", "방탄소년단(BTS)"),
//        AlbumModel(R.drawable.img_album_exp2, "LILAC", "아이유(IU)"),
//        AlbumModel(R.drawable.img_album_exp3, "Album Three", "Artist C"),
//        AlbumModel(R.drawable.img_album_exp4, "Album Four", "Artist D"),
//        AlbumModel(R.drawable.img_album_exp5, "Album Five", "Artist E")
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMain2ndBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

//        //리사이클러뷰부분
//        val recyclerView = findViewById<RecyclerView>(R.id.album_recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = AlbumAdapter(albumList)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment

        //네비게이션을 컨트롤러
        val navController = navHostFragment.navController
        //바텀네비게이션뷰와 묶어줌
        NavigationUI.setupWithNavController(mBinding.myBtmNav, navController)
    }

}