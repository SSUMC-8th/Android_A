package com.example.umc_8th.flo_project

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OnboardingService {
    @POST("join")
    fun signUp(@Body request: SignUpRequest): Call<AuthResponse>
}