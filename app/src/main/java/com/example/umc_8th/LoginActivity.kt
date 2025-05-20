package com.example.umc_8th


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import umc.study.umc_8th.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmailId: EditText
    private lateinit var etEmailDomain: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Firebase 인증 객체 초기화
        auth = FirebaseAuth.getInstance()

        // 뷰 바인딩
        etEmailId = findViewById(R.id.login_id_et)
        etEmailDomain = findViewById(R.id.login_direct_input_et)
        etPassword = findViewById(R.id.login_password_et)
        btnLogin = findViewById(R.id.login_sign_in_btn)

        btnLogin.setOnClickListener {
            val email = etEmailId.text.toString().trim() + "@" + etEmailDomain.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // 이메일 형식 검사
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "올바른 이메일 형식을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginWithFirebase(email, password)
        }
    }

    private fun loginWithFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    SharedPreferencesHelper.saveUserIdx(this, uid)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "로그인 실패: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    Log.e("LoginActivity", "로그인 실패", task.exception)
                }
            }
    }
}
