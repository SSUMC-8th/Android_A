package com.example.umc_8th.flo_project

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName(value= "isSuccess")val isSuccess: Boolean,
    @SerializedName(value= "code")val code: String,
    @SerializedName(value= "message")val message: String,
    @SerializedName(value= "result")val result: Result?
)

data class Result(
    @SerializedName(value= "memberId")var memberId: Int,
    @SerializedName(value= "accessToken")var accessToken: String
)