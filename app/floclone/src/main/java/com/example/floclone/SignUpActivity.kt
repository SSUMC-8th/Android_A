package com.example.floclone

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import android.graphics.Color
import android.widget.Toast
import com.example.floclone.api.AuthService
import com.example.floclone.api.AuthView
import com.example.floclone.api.LoginResult
import com.example.floclone.api.SignUpResult
import com.google.firebase.auth.FirebaseAuth


class SignUpActivity : AppCompatActivity(), AuthView {

    private lateinit var etId : EditText
    private lateinit var etEmail : EditText
    private lateinit var etPw : EditText
    private lateinit var etPwcheck : EditText
    private lateinit var btnSignin : MaterialButton
    private lateinit var tvError : TextView

    //api 추가
    private lateinit var authService: AuthService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        etId = findViewById<EditText>(R.id.edt_id_signActivity)
        etEmail = findViewById<EditText>(R.id.edt_email_signActivity)
        etPw = findViewById<EditText>(R.id.edt_password_signActivity)
        etPwcheck = findViewById<EditText>(R.id.edt_passwordcheck_signActivity)
        btnSignin = findViewById<MaterialButton>(R.id.btn_signin_signActivity)
        tvError = findViewById<TextView>(R.id.tv_errorsign_signActivity)

        authService = AuthService(this)

        // 모든 editText에 대해 돌면서 체크
        listOf(etId, etEmail, etPw, etPwcheck).forEach { editText ->
            editText.doAfterTextChanged {
                validInputcheck()
            }
        }

        btnSignin.setOnClickListener {
            //signUserInfo()

            //외부 API를 이용해 핸들
            signUserInfoWithApi()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun signUserInfoWithApi(){
        var id = etId.text.toString().trim()
        var email = etEmail.text.toString().trim()
        var pw = etPw.text.toString().trim()
        var userId = "$id@$email"
        authService.signUp("uhutcha",userId,pw)
    }

    private fun validInputcheck(){
        //일단 editText 다 받기
        var id = etId.text.toString().trim()
        var email = etEmail.text.toString().trim()
        var pw = etPw.text.toString().trim()
        var pwCheck = etPwcheck.text.toString().trim()

        //비밀번호 check
        if(pw.length < 6){
            tvError.text = "비밀번호는 최소 6자리 이상이어야 합니다."
            tvError.visibility = TextView.VISIBLE
        }
        else if(pwCheck.length >= 1 && !pw.equals(pwCheck)){
            tvError.text = "비밀번호가 일치하지 않습니다."
            tvError.visibility = TextView.VISIBLE
        }
        else{
            tvError.visibility = TextView.INVISIBLE
        }

        var isValid = false
        if(!id.isEmpty() && !email.isEmpty() && pw.length>=6 && pw.equals(pwCheck)){
            isValid = true
        }

        if(isValid){
            btnSignin.isEnabled = true
            btnSignin.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3E42FF"))
        }
        else{
            btnSignin.isEnabled = false
            btnSignin.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#919194"))
        }

    }

    private fun signUserInfo(){
        var id = etId.text.toString().trim()
        var email = etEmail.text.toString().trim()
        var pw = etPw.text.toString().trim()
        var userId = "$id@$email"

        val auth = FirebaseAuth.getInstance()
        val dbUser = FirebaseDatabase.getInstance().getReference("User")

        //1. FirebaseAuth를 통한 회원가입
        auth.createUserWithEmailAndPassword(userId, pw)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener

                    //실제 로그인 검증인 auth로 여기는 uid와 email만 (비밀번호는 보안상X)
                    val userData = mapOf(
                        "uid" to uid,
                        "email" to userId
                    )

                    //push 시 랜덤 key 생성
                    dbUser.child(uid).setValue(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "회원가입 완료!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "DB 저장 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                        }

                }

                else{
                    Toast.makeText(this, "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

    }

    override fun onSignUpSuccess(result: SignUpResult) {

        runOnUiThread {

            var id = etId.text.toString().trim()
            var email = etEmail.text.toString().trim()
            var userId = "$id@$email"

            var uid = result.memberId.toString()

            val dbUser = FirebaseDatabase.getInstance().getReference("User")
            val userData = mapOf(
                "uid" to uid,
                "email" to userId
            )
            dbUser.child(uid).setValue(userData)

            Toast.makeText(
                this,
                "회원가입 성공! ID=${result.memberId}",
                //현재 ID = 14
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    override fun onSignUpFailure(errorMsg: String) {

        Toast.makeText(this, "${errorMsg}", Toast.LENGTH_SHORT).show()
    }

    override fun onLoginSuccess(result: LoginResult) {

    }

    override fun onLoginFailure(errorMsg: String) {

    }

}