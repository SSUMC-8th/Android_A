package com.example.umc_8th.flo_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.umc_8th.R
import com.example.umc_8th.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginSignInBtn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        Log.d("LOGIN", "login() 호출됨")
        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.loginPasswordEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val email : String = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val pwd : String = binding.loginPasswordEt.text.toString()

//        val songDB = SongDatabase.getInstance(this)!!
//        val user = songDB.userDao().getUser(email, pwd)
//
//        if (user != null) {
////            saveJwt(user.id)
//            startMainActivity()
//        } else {
//            Toast.makeText(this, "회원정보가 존재하지 않습니다", Toast.LENGTH_SHORT).show()
//        }
        Log.d("LOGIN", "이메일: $email, 비밀번호: $pwd")
        val authService = AuthService()
        authService.setLoginView(this)
        authService.login(User(email, pwd, ""))
    }

//    private fun saveJwt(jwt : Int) {
//        val spf = getSharedPreferences("auth", MODE_PRIVATE)
//        val editor = spf.edit()
//
//        editor.putInt("jwt", jwt)
//        editor.apply()
//    }
    private fun saveJwt2(jwt : String) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }


    private fun startMainActivity() {
        val intent = Intent(this, FloMainActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginSuccess(code: String, result: Result) {
        Log.d("LOGIN", "onLoginSuccess 호출됨, code=$code, token=${result.accessToken}")
        when(code){
            "1000" -> {
                saveJwt2(result.accessToken)
                startMainActivity()
            }
        }
    }

    override fun onLoginFailure() {
        Toast.makeText(this, "로그인에 실패했습니다. 인터넷 연결 또는 서버 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show()
    }

}