package com.example.umc_8th

data class SignUpResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: SignUpResult?
)

data class SignUpResult(
    val memberId: Int,
    val createdAt: String,
    val updatedAt: String
)