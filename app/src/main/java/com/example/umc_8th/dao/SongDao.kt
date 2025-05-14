package com.example.umc_8th.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.umc_8th.entity.SongEntity

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songs: List<SongEntity>)

    @Query("SELECT * FROM SongTable")
    suspend fun getAll(): List<SongEntity>

    @Query("SELECT * FROM SongTable WHERE songId = :id")
    suspend fun getSongById(id: Int): SongEntity?

    @Query("UPDATE SongTable SET isLiked = :isLiked WHERE songId = :songId")
    suspend fun updateIsLiked(songId: Int, isLiked: Boolean)

    @Query("SELECT * FROM SongTable WHERE albumId = :albumId LIMIT 1")
    fun getFirstSongByAlbumId(albumId: Int): SongEntity?

    @Query("SELECT * FROM SongTable WHERE albumId = :albumId ORDER BY songId ASC")
    suspend fun getSongsByAlbumId(albumId: Int): List<SongEntity>

    @Query("SELECT * FROM SongTable WHERE isLiked = 1")
    fun getLikedSongs(): androidx.lifecycle.LiveData<List<SongEntity>>


}

