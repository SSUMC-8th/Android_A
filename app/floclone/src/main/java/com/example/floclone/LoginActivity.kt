package com.example.floclone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var etId : EditText
    private lateinit var etEmail : EditText
    private lateinit var etPw : EditText

    private lateinit var btnlogin : MaterialButton
    private lateinit var btnSingin : TextView
    private lateinit var tvError : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        etId = findViewById<EditText>(R.id.edt_id_loginActivity)
        etEmail = findViewById<EditText>(R.id.edt_email_loginActivity)
        etPw = findViewById<EditText>(R.id.edt_password_loginActivity)
        btnSingin = findViewById<TextView>(R.id.btn_signin_loginActivity)
        btnlogin = findViewById<MaterialButton>(R.id.btn_login_loginActivity)
        tvError = findViewById<TextView>(R.id.tv_errorlogin_loginActivity)


        btnlogin.setOnClickListener {
            validLogin()
        }

        btnSingin.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validLogin(){
        var id = etId.text.toString().trim()
        var email = etEmail.text.toString().trim()
        var pw = etPw.text.toString().trim()

        var userId = "$id@$email"

        if (id.isEmpty() || email.isEmpty() || pw.isEmpty()) {
            tvError.text = "잘못된 입력입니다."
            tvError.visibility = TextView.VISIBLE
            return
        }

        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(userId, pw)
            .addOnCompleteListener { task ->
                //성공
                if(task.isSuccessful){
                    //1. 에러 메시지 없애기
                    tvError.visibility = TextView.INVISIBLE

                    //2. auth로부터 uid 받고, 이를 sharedPreference에 저장
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val sharedPref = getSharedPreferences("login", MODE_PRIVATE)

                    sharedPref.edit()
                        .putBoolean("loginCheck", true)
                        .putString("id", uid)
                        .apply()

                    //3. 로그인 성공
                    Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                //실패
                else {
                    val exception = task.exception
                    when (exception) {
                        is FirebaseAuthInvalidUserException -> {
                            tvError.text = "존재하지 않는 계정입니다."
                            tvError.visibility = TextView.VISIBLE
                        }

                        is FirebaseAuthInvalidCredentialsException -> {
                            tvError.text = "아이디 또는 비밀번호가 틀렸습니다."
                            tvError.visibility = TextView.VISIBLE
                        }

                        else -> {
                            Toast.makeText(
                                this,
                                "로그인 실패: ${exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
    }


}