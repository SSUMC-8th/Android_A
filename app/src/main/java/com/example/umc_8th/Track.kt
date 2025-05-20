package com.example.umc_8th


data class Track(
    val title: String,
    val artist: String,
    val musicResId: Int
)

fun Track.toSong(): Song {
    return Song(
        title = this.title,
        singer = this.artist,
        second = 0,
        playTime = 240000, // 임의의 기본값 또는 track에서 받아온 값
        isPlaying = false,
        music = "", // 음악 파일명이나 URI
        coverImg = null, // 앨범 커버 이미지 리소스
        isLike = false
    )
}