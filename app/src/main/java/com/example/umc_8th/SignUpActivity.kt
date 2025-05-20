// SignUpActivity.kt
package com.example.umc_8th

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import umc.study.umc_8th.R

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.emailEditText)
        etPassword = findViewById(R.id.passwordEditText)
        btnSignUp = findViewById(R.id.signupBtn)

        btnSignUp.setOnClickListener {
            val emailDomainSpinner = findViewById<Spinner>(R.id.emailDomainSpinner)
            val email = etEmail.text.toString().trim() + "@" + emailDomainSpinner.selectedItem.toString()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signUpWithFirebase(email, password)
            //startActivity(Intent(this, LoginActivity::class.java))
        }

    }

    private fun signUpWithFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        saveUserToRealtimeDatabase(user)
                        Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(this, "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    Log.e("SignUpActivity", "회원가입 실패", task.exception)
                }
            }
    }

    private fun saveUserToRealtimeDatabase(user: FirebaseUser) {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")

        val userData = User(
            uid = user.uid,
            email = user.email ?: ""
        )

        usersRef.child(user.uid).setValue(userData)
            .addOnSuccessListener {
                Log.d("FirebaseDB", "사용자 정보 저장 성공")
            }
            .addOnFailureListener {
                Log.e("FirebaseDB", "사용자 정보 저장 실패: ${it.message}")
            }
    }
}
