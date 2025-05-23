package com.example.floclone.api

interface AuthView {

    // 회원가입 성공 시, 백엔드가 준 SignUpResult 전체를 넘겨줌
    fun onSignUpSuccess(result: SignUpResult)

    // 회원가입 실패 시, errorMsg에 실패 사유(코드·메시지)를 담아 넘겨줌
    fun onSignUpFailure(errorMsg: String)

    // 로그인 성공 시, 백엔드가 준 LoginResult 전체를 넘겨줌
    fun onLoginSuccess(result: LoginResult)

    // 로그인 실패 시, errorMsg에 실패 사유(코드·메시지)를 담아 넘겨줌
    fun onLoginFailure(errorMsg: String)


}