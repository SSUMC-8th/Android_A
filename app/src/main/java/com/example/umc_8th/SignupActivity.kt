package com.example.umc_8th

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_8th.databinding.ActivitySignupBinding
import com.example.umc_8th.api.AuthService
import com.example.umc_8th.api.AuthView
import com.example.umc_8th.api.LoginResponse
import com.example.umc_8th.api.SignUpRequest
import com.example.umc_8th.api.SignUpResponse

class SignupActivity : AppCompatActivity(), AuthView {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authService = AuthService(this)

        binding.btnRegister.setOnClickListener {

            val name = "사용자이름" // 이름 입력 칸이 있다면 여기에 추가
            val email = binding.editTextUsername.text.toString().trim()
            val password = binding.editTextEmail.text.toString().trim()
            val passwordConfirm = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != passwordConfirm) {
                Toast.makeText(this, "비밀번호가     일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val signUpRequest = SignUpRequest(name, email, password)
            authService.signUp(signUpRequest)
        }
    }

    override fun onSignUpSuccess(response: SignUpResponse) {
        Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
        finish() // 회원가입 성공 시 로그인 화면으로 돌아가기 등
    }

    override fun onSignUpFailure(message: String) {
        Toast.makeText(this, "회원가입 실패: $message", Toast.LENGTH_LONG).show()
    }

    // Login 관련 콜백은 구현 안 해도 됨
    override fun onLoginSuccess(response: LoginResponse) {}
    override fun onLoginFailure(message: String) {}
}


//package com.example.umc_8th
//
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.umc_8th.databinding.ActivitySignupBinding
//import com.google.firebase.auth.FirebaseAuth
//
//
//class SignupActivity : AppCompatActivity() {
//
//    private lateinit var mbinding: ActivitySignupBinding
//    //firebase설정
//    val auth = FirebaseAuth.getInstance()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mbinding = ActivitySignupBinding.inflate(layoutInflater)
//        setContentView(mbinding.root)
//
//        mbinding.btnRegister.setOnClickListener {
//            val email = mbinding.editTextUsername.text.toString().trim()
//            val password = mbinding.editTextEmail.text.toString().trim()
//            val passwordConfirm = mbinding.editTextPassword.text.toString().trim()
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
//            if (password != passwordConfirm) {
//                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // Firebase 회원가입 시도
//            auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
//                        finish() // 가입 성공 후 종료하거나 로그인 화면으로 이동
//                    } else {
//                        Toast.makeText(this, "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_LONG).show()
//                    }
//                }
//        }
//
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//    }
//}