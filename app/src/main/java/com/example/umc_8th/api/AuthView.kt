package com.example.umc_8th.api

//API 호출 결과를 액티비티에 전달하는 콜백 함수 정의
//액티비티에 정의됨

interface AuthView {
    fun onSignUpSuccess(response: SignUpResponse)
    fun onSignUpFailure(message: String)

    fun onLoginSuccess(response: LoginResponse)
    fun onLoginFailure(message: String)
}
