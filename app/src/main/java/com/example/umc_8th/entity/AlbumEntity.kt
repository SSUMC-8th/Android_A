package com.example.umc_8th.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.umc_8th.AlbumItem
import com.example.umc_8th.R

@Entity(tableName = "AlbumTable")
data class AlbumEntity(
    @PrimaryKey val albumId: Int, // autoGenerate 제거
    var title: String = "",
    var singer: String = "",
    var coverImg: Int? = null
)
fun AlbumEntity.toAlbumItem(): AlbumItem {
    return AlbumItem(
        albumId = this.albumId,  // 추가
        albumImage = this.coverImg ?: R.drawable.img_first_album_default,
        albumName = this.title,
        artistName = this.singer
    )
}


