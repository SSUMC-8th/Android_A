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

    //특정 ID 앨범들 가져오기 (1,2,3...)
    @Query("SELECT * FROM AlbumTable WHERE albumId IN (:albumIds)")
    suspend fun getAlbumsByIds(albumIds: List<Int>): List<AlbumEntity>

    //특정 가수만 가져오기
    @Query("SELECT * FROM AlbumTable WHERE singer = :singerName")
    suspend fun getAlbumsBySinger(singerName: String): List<AlbumEntity>


}

