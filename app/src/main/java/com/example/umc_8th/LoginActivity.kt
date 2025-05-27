package com.example.umc_8th


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Response
import umc.study.umc_8th.R
import retrofit2.Callback

class LoginActivity : AppCompatActivity() {

    private lateinit var loginIdEt: EditText
    private lateinit var loginDirectInputEt: EditText
    private lateinit var loginPasswordEt: EditText
    private lateinit var loginSignInBtn: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginIdEt = findViewById(R.id.login_id_et)
        loginDirectInputEt = findViewById(R.id.login_direct_input_et)
        loginPasswordEt = findViewById(R.id.login_password_et)
        loginSignInBtn = findViewById(R.id.login_sign_in_btn)

        loginSignInBtn.setOnClickListener {
            val email = "${loginIdEt.text}@${loginDirectInputEt.text}"
            val password = loginPasswordEt.text.toString()
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        val request = LoginRequest(email, password)

        RetrofitClient.apiService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        Log.d("LoginActivity", "로그인 성공: ${body.result}")
                        Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("LoginActivity", "로그인 실패: ${body?.message}")
                        Toast.makeText(this@LoginActivity, "로그인 실패: ${body?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("LoginActivity", "서버 오류: ${response.errorBody()?.string()}")
                    Toast.makeText(this@LoginActivity, "서버 오류", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "통신 실패: ${t.message}")
                Toast.makeText(this@LoginActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
