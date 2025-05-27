package com.example.umc_8th

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/join")
    fun signUp(@Body request: SignUpRequest): Call<SignUpResponse>

    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}