package com.example.umc_8th.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class AlbumEntity(
    var title: String = "",
    var singer: String = "",
    var coverImg: Int? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
