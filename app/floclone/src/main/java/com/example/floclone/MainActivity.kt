package com.example.floclone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var clHomeplayer : ConstraintLayout

    //registerForActivityResult
    private val songResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val returnString = result.data?.getStringExtra("title")
            Toast.makeText(this, "Title: $returnString", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var title = findViewById<TextView>(R.id.tv_homeplayer_title).text.toString()
        var artist = findViewById<TextView>(R.id.tv_homeplayer_artist).text.toString()

        //fragmentManager랑 navController 사용  fragment 이동 관리 + 백스택
        //FragmentContainer를 HavHostFragment처럼 사용하자
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.ct_home) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnv_home)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnItemSelectedListener { item ->
            //백스택에 fragment들이 쌓이는 것을 막기
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_home_bottom_graph, true) //전체 그래프에 popup옵션(그래프 ID를 연결)
                .build()

            when (item.itemId) {
                R.id.navigation_homeFragment -> {
                    navController.navigate(R.id.navigation_homeFragment, null, navOptions)
                    true
                }


                else -> false
            }
        }


        //플레이 바랑 연결해서 노래 액티비티로 이동
        clHomeplayer = findViewById(R.id.cl_homeplayer)
        clHomeplayer.setOnClickListener({
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title",title)
            intent.putExtra("artist", artist)
            intent.putExtra("lyric1", "例えば僕ら二人 煌めく映画のように")
            intent.putExtra("lyric2", "出会いなおせたらどうしたい")

            //intent를 만들고 startActivity 대신 registerForActivityResult를 수행
            songResultLauncher.launch(intent)
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

}