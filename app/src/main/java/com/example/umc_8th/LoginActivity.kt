package com.example.umc_8th

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_8th.api.AuthService
import com.example.umc_8th.api.AuthView
import com.example.umc_8th.api.LoginRequest
import com.example.umc_8th.api.LoginResponse
import com.example.umc_8th.api.SignUpResponse
import com.example.umc_8th.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), AuthView {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authService = AuthService(this) // this는 AuthView 구현한 LoginActivity 자신


        binding.btnLogin.setOnClickListener {
            val email = binding.editTextUsername.text.toString().trim()
            val password = binding.editTextEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(email, password)
            authService.login(loginRequest)
        }

        binding.signInBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    //결국 로그인 수행 코드는 여기
    override fun onLoginSuccess(response: LoginResponse) {
        // 로그인 성공시 SharedPreferences에 저장
        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
        prefs.edit().putBoolean("isLoggedIn", true).apply()

        // 저장 직후 상태 확인
        val value = prefs.getBoolean("isLoggedIn", false)
        Log.d("AUTH_LOG", "LoginActivity 저장 직후 읽은 값: $value")

        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity_2nd::class.java))
        finish()
    }


//    override fun onLoginSuccess(response: LoginResponse) {
//        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
//
//        // 토큰 저장, 다음 화면 이동 등 처리
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
//    }

    override fun onLoginFailure(message: String) {
        Toast.makeText(this, "로그인 실패: $message", Toast.LENGTH_LONG).show()
    }

    // SignUp 관련 콜백은 구현 안 해도 됨
    override fun onSignUpSuccess(response: SignUpResponse) {}
    override fun onSignUpFailure(message: String) {}
}


//package com.example.umc_8th
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.AttributeSet
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.umc_8th.databinding.ActivityLoginBinding
//import com.google.firebase.auth.FirebaseAuth
//
//class LoginActivity : AppCompatActivity() {
//
//    private lateinit var mbinding: ActivityLoginBinding
//    private val auth = FirebaseAuth.getInstance()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mbinding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(mbinding.root)
//
//        mbinding.btnLogin.setOnClickListener {
//            val email = mbinding.editTextUsername.text.toString().trim()
//            val password = mbinding.editTextEmail.text.toString().trim()
//
//            if (email.isEmpty()) {
//                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            if (password.isEmpty()) {
//                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // Firebase 로그인 시도
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
//                        // 로그인 성공 후 메인 화면 이동 (예시)
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
//
//                    } else {
//                        Toast.makeText(this, "로그인 실패: ${task.exception?.message}", Toast.LENGTH_LONG).show()
//                    }
//                }
//        }
//
//        mbinding.signInBtn.setOnClickListener {
//            val intent = Intent(this, SignupActivity::class.java)
//            startActivity(intent)
//        }
//    }
//
//
//    override fun onDestroy() {
//        super.onDestroy()
//    }
//}