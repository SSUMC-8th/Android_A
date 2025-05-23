package com.example.floclone.api


//여기서 정의한 데이터 클래스를  ApiService.kt에서 사용

//회원가입 요청 보낼 때 양식
data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)

//로그인 요청 보낼 때 양식
data class LoginRequest(
    val email: String,
    val password: String
)

//응답 양식
data class Response<T> (
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T?
)

//응답 양식 중 회원가입
data class SignUpResult(
    val memberId: Long,
    val createdAt: String,
    val updatedAt: String
)

//응답 양식 중 로그인
data class LoginResult(
    val memberId: Long,
    val accessToken: String
)

typealias LoginResponse = Response<LoginResult>
typealias SignUpResponse = Response<SignUpResult>