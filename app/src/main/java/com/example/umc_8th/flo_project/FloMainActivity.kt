package com.example.umc_8th.flo_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.gson.Gson
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivityFloMainBinding

class FloMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityFloMainBinding
    private var song:Song = Song()
    private var gson:Gson = Gson()
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    companion object {const val STRING_INTENT_KEY ="message"}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyApp_Splash2)
        installSplashScreen()
        enableEdgeToEdge()
        binding=ActivityFloMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inputDummySongs()
        initPlayList()
        initBottomNavigation()
//        BottomNav()

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val message = data.getStringExtra("message")
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
//        하단 미니 플레이어에서 SongAcitivitiy로 데이터 전달하고 띄우기
//        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 0,60,false, "sample")
        binding.mainPlayerCl.setOnClickListener{
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this,SongActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }
//    override fun onStart() {
//        super.onStart()
//
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songId = sharedPreferences.getInt("songId", 0)
//        val songDB = SongDatabase.getInstance(this)!!
//
//        song = if (songId == 0){
//            songDB.songDao().getSong(1)
//        } else{
//            songDB.songDao().getSong(songId)
//        }
//        setMiniPlayer(song)
//    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        setMiniPlayer(songs[nowPos])
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragmnet -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragmnet -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun setMiniPlayer(song : Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val second = sharedPreferences.getInt("second", 0)
        binding.mainMiniplayerProgress.progress = (second * 100000 / song.playTime)
    }

    fun updateMainPlayerCl(album : Album) {
        binding.mainMiniplayerTitleTv.text = album.title
        binding.mainMiniplayerSingerTv.text = album.singer
        binding.mainMiniplayerProgress.progress = 0
    }

    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
                3
            )
        )

        songDB.songDao().insert(
            Song(
                "Text",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_text",
                R.drawable.img_album_exp3,
                false,
                4
            )
        )


        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "music_boy",
                0,
                230,
                false,
                "music_boy",
                R.drawable.img_album_exp4,
                false,
                5
            )
        )


        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,
                false,
                6
            )
        )
        val songDBData = songDB.songDao().getSongs()
    }

}

//    private val getResultText =registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ){ result ->
//        if(result.resultCode == Activity.RESULT_OK){
//            val returnString = result.data?.getStringExtra(STRING_INTENT_KEY)
//            val toast = Toast.makeText(this, returnString, Toast.LENGTH_LONG)
//            toast.show()
//        }
//    }
//    private fun BottomNav(){
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.main_frame, HomeFragment())
//            .commitAllowingStateLoss()
//
//        binding.mainBnv.setOnItemSelectedListener { item ->
//            when(item.itemId){
//                R.id.homeFragmnet->{
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_frame, HomeFragment())
//                        .commitAllowingStateLoss()
//                    return@setOnItemSelectedListener true
//                }
//                R.id.lookFragment->{
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_frame, LookFragment())
//                        .commitAllowingStateLoss()
//                    return@setOnItemSelectedListener true
//                }
//                R.id.searchFragmnet->{
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_frame, SearchFragment())
//                        .commitAllowingStateLoss()
//                    return@setOnItemSelectedListener true
//                }
//                R.id.lockerFragment->{
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_frame, LockerFragment())
//                        .commitAllowingStateLoss()
//                    return@setOnItemSelectedListener true
//                }
//            }
//            false
//        }
//    }
//
//    private fun setMiniPlayer(song:Song){
//        binding.mainMiniplayerTitleTv.text=song.title
//        binding.mainMiniplayerSingerTv.text=song.singer
//        binding.mainMiniplayerProgress.progress=(song.second*100000)/song.playTime
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val sharedPreferences=getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        song=if(songJson==null){
//            Song("라일락", "아이유(IU)", 0, 60, false, "sample")
//        }else{
//            gson.fromJson(songJson, song::class.java)
//        }
//        setMiniPlayer(song)
//    }
