// SignUpActivity.kt
package com.example.umc_8th

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import umc.study.umc_8th.R

class SignUpActivity : AppCompatActivity(), SignUpView {

    private lateinit var emailEditText: EditText
    private lateinit var emailSpinner: Spinner
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signupBtn: Button

    private lateinit var service: SignUpService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        emailEditText = findViewById(R.id.emailEditText)
        emailSpinner = findViewById(R.id.emailDomainSpinner)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        signupBtn = findViewById(R.id.signupBtn)

        service = SignUpService(this)

        signupBtn.setOnClickListener {
            val email = "${emailEditText.text}@${emailSpinner.selectedItem}"
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val name = emailEditText.text.toString().substringBefore("@") // 이름은 이메일 앞부분으로 대체

            if (password != confirmPassword) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            service.signUp(name, email, password)
        }
    }

    override fun onSignUpSuccess(message: String) {
        Log.d("SignUpActivity", "회원가입 성공: $message")
        Toast.makeText(this, "회원가입 성공: $message", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSignUpFailure(message: String) {
        Log.d("SignUpActivity", "회원가입 실패: $message")
        Toast.makeText(this, "회원가입 실패: $message", Toast.LENGTH_SHORT).show()
    }
}
