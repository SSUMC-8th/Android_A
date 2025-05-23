package com.example.floclone.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

//각 API 서비스를 제공하는 객체
interface ApiService {
    //notation으로 정의된 거 작성(회원가입은 POST)

    @POST("join")
    fun signUP(@Body req: SignUpRequest): Call<SignUpResponse>

    @POST("login")
    fun login(@Body req: LoginRequest): Call<LoginResponse>

}