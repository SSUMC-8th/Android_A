package com.example.umc_8th.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.umc_8th.entity.AlbumEntity

@Dao
interface AlbumDao {
    @Query("SELECT * FROM AlbumTable")
    suspend fun getAlbums(): List<AlbumEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumEntity>)
}

