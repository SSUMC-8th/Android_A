package com.example.floclone

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Banner(
    val bannerTitle: String,
    val color: String,
    val info: String,
    val song1: Song,
    val song2: Song,
) : Parcelable