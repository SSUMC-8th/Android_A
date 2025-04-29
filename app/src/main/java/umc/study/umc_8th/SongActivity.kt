package umc.study.umc_8th

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SongActivity : AppCompatActivity() {
    private lateinit var seekBar: SeekBar
    private lateinit var btnPlay: ImageButton
    private lateinit var btnArrowDown: ImageButton
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView

    private val handler = Handler(Looper.getMainLooper())
    private var updateRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        MediaPlayerManager.initialize(this)

        seekBar = findViewById(R.id.seekBar)
        btnPlay = findViewById(R.id.btn_start)
        btnArrowDown = findViewById(R.id.btn_arrowDown_songActivity)
        startTime = findViewById(R.id.startTime)
        endTime = findViewById(R.id.endTime)

        seekBar.max = MediaPlayerManager.getDuration()
        endTime.text = formatTime(MediaPlayerManager.getDuration())

        btnPlay.setOnClickListener {
            if (MediaPlayerManager.isPlaying) {
                MediaPlayerManager.pause()
                btnPlay.setImageResource(R.drawable.btn_miniplay_mvplay)
                stopSeekBarUpdate()
            } else {
                MediaPlayerManager.play()
                btnPlay.setImageResource(R.drawable.btn_miniplay_pause)
                startSeekBarUpdate()
            }
        }

        btnArrowDown.setOnClickListener {
            finish() // MainActivity로 돌아감 (MediaPlayer는 계속 재생 중)
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
            btnPlay.setImageResource(R.drawable.btn_miniplay_pause)
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

