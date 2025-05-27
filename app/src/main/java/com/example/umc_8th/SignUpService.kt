package com.example.umc_8th

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpService(private val view: SignUpView) {

    fun signUp(name: String, email: String, password: String) {
        val request = SignUpRequest(name, email, password)

        RetrofitClient.apiService.signUp(request)
            .enqueue(object : Callback<SignUpResponse> {
                override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.isSuccess == true) {
                            view.onSignUpSuccess(body.message)
                        } else {
                            view.onSignUpFailure(body?.message ?: "알 수 없는 오류")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = parseErrorMessage(errorBody)
                        view.onSignUpFailure(errorMessage)
                    }
                }

                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    view.onSignUpFailure("서버 오류: ${t.localizedMessage}")
                }
            })
    }

    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            val json = JSONObject(errorBody ?: "")
            json.getString("message")
        } catch (e: Exception) {
            "서버 응답 오류"
        }
    }
}