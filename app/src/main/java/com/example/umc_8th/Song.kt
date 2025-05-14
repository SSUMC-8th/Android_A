package com.example.umc_8th

data class Song(
    val title: String,
    val artist: String,
    val albumResId: Int,
    var isSelected: Boolean = false // 🔸 스위치 상태 저장
)