package umc.study.umc_8th

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class TaskThree : AppCompatActivity() {

    private lateinit var miniPlayer: ConstraintLayout
    private lateinit var seekBar: SeekBar
    private lateinit var btnPlay: ImageButton
    private lateinit var startTime: TextView

    private val handler = Handler(Looper.getMainLooper())
    private var updateRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_three)

        MediaPlayerManager.initialize(this)

        miniPlayer = findViewById(R.id.miniPlayer)
        seekBar = findViewById(R.id.seekBar)
        btnPlay = findViewById(R.id.btn_play)
        startTime = findViewById(R.id.startTime)

        seekBar.max = MediaPlayerManager.getDuration()

        miniPlayer.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        btnPlay.setOnClickListener {
            if (MediaPlayerManager.isPlaying) {
                MediaPlayerManager.pause()
                btnPlay.setImageResource(R.drawable.btn_miniplayer_play)
                stopSeekBarUpdate()
            } else {
                MediaPlayerManager.play()
                btnPlay.setImageResource(R.drawable.btn_miniplay_pause)
                startSeekBarUpdate()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MediaPlayerManager.seekTo(progress)
                    startTime.text = formatTime(progress)
                }
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        if (MediaPlayerManager.isPlaying) {
            btnPlay.setImageResource(R.drawable.btn_miniplay_mvpause)
            startSeekBarUpdate()
        }
    }

    private fun startSeekBarUpdate() {
        updateRunnable = object : Runnable {
            override fun run() {
                seekBar.progress = MediaPlayerManager.getCurrentPosition()
                startTime.text = formatTime(MediaPlayerManager.getCurrentPosition())
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(updateRunnable!!)
    }

    private fun stopSeekBarUpdate() {
        updateRunnable?.let { handler.removeCallbacks(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSeekBarUpdate()
    }

    private fun formatTime(ms: Int): String {
        val minutes = ms / 1000 / 60
        val seconds = (ms / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
