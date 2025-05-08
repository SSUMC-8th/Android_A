package com.example.floclone.database
import androidx.room.*

@Dao
interface SongDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song)

    @Update
    suspend fun updateSong(song: Song)

    @Delete
    suspend fun deleteSong(song: Song)

    @Query("SELECT * FROM SongTable")
    suspend fun getAllSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    suspend fun getSongById(id: Int): Song?

    @Query("SELECT * FROM SongTable WHERE isLike = 1")
    suspend fun getLikedSongs(): List<Song>

    @Query("UPDATE SongTable SET coverImg = :newRes WHERE coverImg = :oldRes")
    suspend fun updateCoverImg(oldRes: Int, newRes: Int)

}