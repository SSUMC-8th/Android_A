package com.example.umc_8th

import umc.study.umc_8th.R

data class Album(
    val title: String,
    val artist: String,
    val coverImage: Int, // 이미지 리소스 ID
    val trackList: List<Track>
)
val albumList = listOf(
    Album("LILAC", "아이유 (IU)", R.drawable.img_album_exp2,
        listOf(
            Track("LILAC", "아이유 (IU)", R.raw.lilac),
            Track("Flu", "아이유 (IU)", R.raw.flu),
            Track("Coin", "아이유 (IU)", R.raw.coin),
            Track("봄 안녕 봄", "아이유 (IU)", R.raw.hi_spring_bye)
        )
        ),
    Album("PERSONA", "방탄소년단 (BTS)", R.drawable.img_album_exp4,
        listOf(
            Track("작은 것들을 위한 시(feat.Halsey)", "방탄소년단 (BTS)", R.raw.lilac),
            Track("소우주", "방탄소년단(BTS)", R.raw.flu),
            Track("Make It Right", "방탄소년단 (BTS)", R.raw.coin),
            Track("HOME", "방탄소년단 (BTS)", R.raw.hi_spring_bye)
        )),
    Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3,
        listOf(
            Track("Next Level", "에스파 (AESPA)", R.raw.lilac)
        )
    )
)