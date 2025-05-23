package com.example.umc_8th.flo_project

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://3.35.121.185"
fun getRetrofit(): Retrofit{
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    return retrofit
}