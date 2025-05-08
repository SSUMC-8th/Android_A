package com.example.umc_8th.flo_project

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
import com.google.gson.Gson
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    lateinit var timer : Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()
//        setPlayer(song)
//        var title : String?=null
//        var singer : String?=null
//        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
//            title = intent.getStringExtra("title")
//            singer =intent.getStringExtra("singer")
//            binding.songTitleTv.text=intent.getStringExtra("title")
//            binding.songSingerTv.text=intent.getStringExtra("singer")
//        }
//        binding.songDownIbtn.setOnClickListener{
////            val intent = Intent(this, FloMainActivity::class.java).apply {
////                putExtra("message", title + " " + singer)
////            }
//            val resultIntent = Intent().apply {
//                putExtra("message", "$title $singer")
//            }
//            setResult(RESULT_OK, resultIntent)
//            finish()
//        }

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
        Log.d("second", songs[nowPos].second.toString())
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
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
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
            Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
        }

        else if (nowPos + direct >= songs.size){
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
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
        setPlayer(songs[nowPos])
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
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if (!isLike){
            binding.songLikeIbtn.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.songLikeIbtn.setImageResource(R.drawable.ic_my_like_off)
        }

    }
    private fun setPlayer(song:Song){
        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.songTimeStartTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songTimeEndTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressV.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        mediaPlayer = MediaPlayer.create(this, music)
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