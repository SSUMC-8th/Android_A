package com.example.floclone

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongPlay(
    val title: String,
    val artist: String,
    val playCheck : Boolean,
    val currentPosition : Int,
    val durationSong : Int,
    val lyric1 : String,
    val lyric2 : String
) : Parcelable