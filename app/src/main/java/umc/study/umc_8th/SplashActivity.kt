package umc.study.umc_8th

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        //지연 시간 후 실행
        Handler(Looper.getMainLooper()).postDelayed({

            //일정 시간이 지나면 매인 엑티비티로 Intent
            val intent = Intent(this, TaskTwoActivity::class.java)
            startActivity(intent)

            //이동한 다음 다시 스플래시 화면으로 이동하는 것 방지
            finish()

        }, 2000) //2초 후 실행

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}