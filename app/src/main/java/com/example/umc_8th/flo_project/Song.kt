package com.example.umc_8th.flo_project

data class Song (
    val title : String ="",
    val singer : String="",
    var second: Int = 0,
    var playTime: Int = 90,
    var isPlaying : Boolean = false
)