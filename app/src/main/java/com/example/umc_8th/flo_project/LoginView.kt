package com.example.umc_8th.flo_project

interface LoginView {
    fun onLoginSuccess(code: String, result:Result)
    fun onLoginFailure()
}