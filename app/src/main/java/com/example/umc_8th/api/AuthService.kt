package com.example.umc_8th.api
//ApiService 호출, 결과 처리 후 AuthView 콜백 호출
//비동기 Retrofit 콜백 처리 담당

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService(private val view: AuthView) {

    private val api = NetworkManager.apiService

    //기재에러없을 시 회원가입 단계
    fun signUp(request: SignUpRequest) {
        api.signUp(request).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    //이게 최종 실핻
                    view.onSignUpSuccess(response.body()!!)
                } else {
                    //현재 계속 여기로 넘어가는중
                    view.onSignUpFailure(response.body()?.message ?: "회원가입 실패")
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                view.onSignUpFailure(t.message ?: "통신 실패")
            }
        })
    }

    fun login(request: LoginRequest) {
        api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    view.onLoginSuccess(response.body()!!)
                } else {
                    view.onLoginFailure(response.body()?.message ?: "로그인 실패")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                view.onLoginFailure(t.message ?: "통신 실패")
            }
        })
    }
}

