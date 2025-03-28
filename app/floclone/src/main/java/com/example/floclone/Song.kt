package com.example.floclone

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


//노래 이름 및 정보가 담긴 클래스
//@Parcelize는 Kotlin에서 객체를 Parcel(묶음 형태)로
// 직렬화해서 전달할 수 있게 해주는 Annotation(어노테이션)이야.
@Parcelize
data class Song(
    val title: String,
    val artist: String,
    val image: Int,
    val albumName: String,
) : Parcelable
