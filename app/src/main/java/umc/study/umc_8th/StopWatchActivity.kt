package umc.study.umc_8th

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StopWatchActivity : AppCompatActivity() {

    private lateinit var timeString: TextView
    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnClear: Button

    private var nowTime = 0
    private var checkrun = false
    private lateinit var timerThread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stop_watch)

        timeString = findViewById<TextView>(R.id.tv_time_stopwatch)
        btnStart = findViewById<Button>(R.id.btn_start_stopwatch)
        btnClear = findViewById<Button>(R.id.btn_clear_stopwatch)
        btnPause = findViewById<Button>(R.id.btn_pause_stopwatch)

        btnStart.setOnClickListener {
            btnStart.visibility = View.GONE
            btnPause.visibility = View.VISIBLE
            checkrun = true
            startTimer()
        }
        btnPause.setOnClickListener {
            btnStart.visibility = View.VISIBLE
            btnPause.visibility = View.GONE
            checkrun = false

        }
        btnClear.setOnClickListener {
            btnStart.visibility = View.VISIBLE
            btnPause.visibility = View.GONE
            checkrun = false
            timeString.text = "00:00.00"
            nowTime = 0
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
    private fun startTimer(){
        timerThread = Thread {
            while (checkrun) {
                try {
                    //0.01초동안 대기(다른 곳에서 반영구적 무한반복을 돌림)
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                if(checkrun){
                    nowTime += 1;

                    runOnUiThread {
                        //여기서 textView update
                        timeString.text = formatetime(nowTime)
                    }
                }
            }
        }
        ////여기까지 timer 정의
        timerThread.start()
    }

    private fun formatetime(now : Int): String{
        //now는 0.01초 단위
        val min = (now / 100) / 60
        val sec = (now / 100) % 60
        val secpoint = now % 100     //0~99

        return String.format("%02d:%02d.%02d", min, sec, secpoint)
    }

    override fun onDestroy() {
        super.onDestroy()
        timerThread.interrupt()
    }
}