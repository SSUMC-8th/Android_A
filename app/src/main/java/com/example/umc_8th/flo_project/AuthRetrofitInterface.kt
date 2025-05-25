package com.example.umc_8th.flo_project

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Headers

interface AuthRetrofitInterface {
    @Headers("Content-Type: application/json")
    @POST("join")
    fun signUp(@Body user:User): Call<AuthResponse>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body req: LoginRequest): Call<AuthResponse>
//    @Headers("Content-Type: application/json")
//    @POST("join")
//    fun signUp(@Body req: SignUpRequest): Call<SignUpResponse>
//
//    @Headers("Content-Type: application/json")
//    @POST("login")
//    fun login(@Body req: LoginRequest): Call<LoginResponse>
}