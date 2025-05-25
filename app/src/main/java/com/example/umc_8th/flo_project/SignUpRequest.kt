package com.example.umc_8th.flo_project

data class SignUpRequest(
    val name:String,
    val email:String,
    val password:String
)
// 로그인
data class LoginRequest(
    val email: String,
    val password: String
)