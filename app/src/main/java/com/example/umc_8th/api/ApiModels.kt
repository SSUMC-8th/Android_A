package com.example.umc_8th.api

data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class Response<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T?
)

data class SignUpResult(val memberId: Long, val createdAt: String, val updatedAt: String)
data class LoginResult(val memberId: Long, val accessToken: String)

typealias SignUpResponse = Response<SignUpResult>
typealias LoginResponse = Response<LoginResult>
