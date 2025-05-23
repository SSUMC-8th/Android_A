package com.example.umc_8th.flo_project

import android.util.Log
import android.view.View
import android.widget.Toast
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView
    fun setSignUpView(signUpView: SignUpView){
        this.signUpView=signUpView
    }
    fun setLoginView(loginView: LoginView){
        this.loginView=loginView
    }
    fun signUp(user:User){

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.signUp(user).enqueue(object: Callback<AuthResponse> {
            override fun onFailure(call: retrofit2.Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.message.toString())
            }

            override fun onResponse(
                call: retrofit2.Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                Log.d("SIGNUP/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!
                when(resp.code){
                    "200" ->signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure()
                }
            }

        })
        Log.d("SIGNUP", "HELLO")
    }

    fun login(user:User){

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.login(user).enqueue(object: Callback<AuthResponse> {
            override fun onFailure(call: retrofit2.Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN/FAILURE", t.message.toString())
            }

            override fun onResponse(
                call: retrofit2.Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                Log.d("LOGIN/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!
                when(val code = resp.code){
                    "1000" -> loginView.onLoginSuccess(code, resp.result!!)
                    "AUTH_014" -> {
                        // TODO: 회원가입 유도, 에러 메시지 표시
                    }
                    "AUTH_008" -> {
                        // 비밀번호 오류
                        // TODO: 비밀번호 재입력 유도
                    }
                    "AUTH_009" -> {
                        // 인증 실패
                        // TODO: 인증 실패 안내
                    }
                    else -> loginView.onLoginFailure()
                }
            }

        })
        Log.d("LOGIN", "HELLO")
    }
}