package com.example.umc_8th.flo_project

import android.util.Log
import android.view.View
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    fun setSignUpView(signUpView: SignUpView){
        this.signUpView=signUpView
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
                    200 ->signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure()
                }
            }

        })
        Log.d("SIGNUP", "HELLO")
    }
}