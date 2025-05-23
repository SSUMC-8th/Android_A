package com.example.floclone.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//실제 API가 있는 서버 주소를 이용해 refrofit 객체를 생성

object NetworkManager {

    // 실제 API 서버의 Base URL IN
    private const val BASE_URL = "https://aos.inyro.site/"
    
    //위의 경우, 실제 각 기능이 실행될 떄, BASE_URL + ApiService에 정의한 URL을 붙여 실행

    
    //로그로 찾자
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()


    // 1. Retrofit 인스턴스 생성
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}