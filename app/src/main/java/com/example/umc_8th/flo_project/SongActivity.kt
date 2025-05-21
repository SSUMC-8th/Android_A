package com.example.umc_8th.flo_project

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.umc_8th.R
import com.example.umc_8th.databinding.ActivitySongBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
//import umc.study.umc_8th.R
//import umc.study.umc_8th.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    lateinit var timer : Timer
    lateinit var database:DatabaseReference
    private var mediaPlayer: MediaPlayer? = null
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference

        initPlayList()
        initSong()
        initClickListener()
        binding.songPlayerPlayIbtn.setOnClickListener{
            PlayerStatus(true)
        }
        binding.songPlayerPauseIbtn.setOnClickListener{
            PlayerStatus(false)
        }
        binding.songLikeIbtn.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }

    }


    override fun onPause() {
        super.onPause()
        songs[nowPos].second = (songs[nowPos].playTime * binding.songProgressV.progress) / 100000
        songs[nowPos].isPlaying = false
        PlayerStatus(false)

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("songId", songs[nowPos].id)
        editor.putInt("second", songs[nowPos].second)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun initPlayList(){
//        songDB = SongDatabase.getInstance(this)!!
//        songs.addAll(songDB.songDao().getSongs())
        // 로컬 샘플 song 데이터
        songs.addAll(getSampleSongs())

        // Firebase에서 좋아요 상태 가져오기
        database.child("likes").get().addOnSuccessListener { snapshot ->
            for (song in songs) {
                val liked = snapshot.child(song.id.toString()).getValue(Boolean::class.java) ?: false
                song.isLike = liked
            }
            setPlayer(songs[nowPos])
        }.addOnFailureListener {
            Toast.makeText(this, "좋아요 상태를 불러오지 못했습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getSampleSongs(): List<Song> {
        return listOf(
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
            ).apply { id = 1 },
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
            ).apply { id = 2 },
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
            ).apply { id = 3 },
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
            ).apply { id = 4 },
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
            ).apply { id = 4 },
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
            ).apply { id = 6 }
        )
    }



    private fun initClickListener(){
        binding.songDownIbtn.setOnClickListener {
            val intent = Intent(this, FloMainActivity::class.java)
            intent.putExtra("message", songs[nowPos].title + "_" + songs[nowPos].singer)
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.songPlayerNextIbtn.setOnClickListener {
            moveSong(+1)
        }
        binding.songPlayerPrevIbtn.setOnClickListener {
            moveSong(-1)
        }
        binding.songPlayerPlayIbtn.setOnClickListener {
            PlayerStatus(true)
        }
        binding.songPlayerPauseIbtn.setOnClickListener {
            PlayerStatus(false)
        }
    }
    private fun moveSong(direct: Int) { // direct는 +1 또는 -1임
        if (nowPos + direct < 0) {
            Snackbar.make(binding.root, "First Song").show()
        }

        else if (nowPos + direct >= songs.size){
            Snackbar.make(binding.root, "last song").show()
        }

        else {
            nowPos += direct
            timer.interrupt()
            startTimer()

            mediaPlayer?.release()
            mediaPlayer = null

            setPlayer(songs[nowPos])
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, FloMainActivity::class.java)
        intent.putExtra("message", "뒤로가기")
        setResult(RESULT_OK, intent)
        finish()
    }

    //프로그래스 바 관련 추가한 부분
    private fun initSong() { // intent 방식 사용 안함
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        startTimer()
//        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun setLike(isLike: Boolean){
//        songs[nowPos].isLike = !isLike
//        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)
//        if (!isLike){
//            binding.songLikeIbtn.setImageResource(R.drawable.ic_my_like_on)
//            Snackbar.make(binding.root, "Liked Song").show()
//        } else{
//            binding.songLikeIbtn.setImageResource(R.drawable.ic_my_like_off)
//            Snackbar.make(binding.root, "Cancel Liked Song").show()
//        }

        //firebase부분
        val currentSong = songs[nowPos]
        currentSong.isLike = !isLike
        database.child("likes").child(currentSong.id.toString())
            .setValue(currentSong.isLike)
        if (currentSong.isLike) {
            binding.songLikeIbtn.setImageResource(R.drawable.ic_my_like_on)
            Snackbar.make(binding.root, "Liked Song").show()
        } else {
            binding.songLikeIbtn.setImageResource(R.drawable.ic_my_like_off)
            Snackbar.make(binding.root, "Cancel Liked Song").show()
        }
        setPlayer(currentSong)
    }

    private fun setPlayer(song:Song){
        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.songTimeStartTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songTimeEndTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressV.progress = (song.second * 1000 / song.playTime)

        val musicResId = when(song.music) {
            "music_bboom" -> R.raw.music_bboom
            "music_boy" -> R.raw.music_boy
            "music_butter" -> R.raw.music_butter
            "music_flu" -> R.raw.music_flu
            "music_lilac" -> R.raw.music_lilac
            "music_text" -> R.raw.music_text
            "sample" -> R.raw.sample
            else -> 0
        }

        if (musicResId != 0) {
            mediaPlayer = MediaPlayer.create(this, musicResId)
        } else {
            Toast.makeText(this, "음원 리소스를 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
            return
        }
        if(song.isLike) {
            binding.songLikeIbtn.setImageResource(R.drawable.ic_my_like_on)
        }
        else {
            binding.songLikeIbtn.setImageResource(R.drawable.ic_my_like_off)
        }
        PlayerStatus(song.isPlaying)
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    fun PlayerStatus(isPlaying:Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying
        if(isPlaying){
            binding.songPlayerPauseIbtn.visibility= View.VISIBLE
            binding.songPlayerPlayIbtn.visibility=View.GONE
            mediaPlayer?.start()
        }
        else{
            binding.songPlayerPlayIbtn.visibility=View.VISIBLE
            binding.songPlayerPauseIbtn.visibility=View.GONE
            if(mediaPlayer?.isPlaying==true) mediaPlayer?.pause()
        }
    }

    inner class Timer(private val playTime : Int, var isPlaying: Boolean = true):Thread(){
        private var second : Int = 0
        private var mills : Float = 0f
        override fun run() {
            super.run()
            try {
                while (true){
                    if(second >= playTime)break
                    if(isPlaying){
                        sleep(50)
                        mills+=50

                        runOnUiThread {
                            binding.songProgressV.progress = ((mills / playTime)*100).toInt()
                        }
                        if(mills % 1000 ==0f){
                            runOnUiThread {
                                binding.songTimeStartTv.text=String.format("%02d: %02d", second / 60, second % 60)

                            }
                            second++
                        }

                    }
                }
            }catch (e:InterruptedException){
                Log.d("Song", "쓰레드 사망 ${e.message}")
            }
        }
    }
}








