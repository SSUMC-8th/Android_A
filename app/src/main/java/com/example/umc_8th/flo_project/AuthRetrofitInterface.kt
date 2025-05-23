package com.example.umc_8th.flo_project

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body

interface AuthRetrofitInterface {
    @POST("/users")
    fun signUp(@Body user:User): Call<AuthResponse>
}