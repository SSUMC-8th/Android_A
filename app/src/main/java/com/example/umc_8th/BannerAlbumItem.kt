package com.example.umc_8th

data class BannerAlbumItem(
    val imageRes: Int,
    val title: String,
    val artist: String,
    val albumInfo: String = "앨범 정보 없음"
)
