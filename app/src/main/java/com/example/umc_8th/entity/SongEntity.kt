package com.example.umc_8th.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class SongEntity(
    var title: String = "",
    var singer: String = "",
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var coverImg: Int? = null,
    var isLiked: Boolean = false,
    var albumId: Int = 0 //앨범과 연결
) {
    @PrimaryKey(autoGenerate = true)
    var songId: Int = 0
}
