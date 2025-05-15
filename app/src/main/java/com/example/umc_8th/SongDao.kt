package com.example.umc_8th

import androidx.room.*

@Dao
interface SongDao {

    @Insert
    fun insert(song: Song): Long

    @Query("SELECT * FROM SongTable WHERE id = :songId")
    fun getSongById(songId: Int): Song?

    @Query("SELECT * FROM SongTable")
    fun getAllSongs(): List<Song>

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("DELETE FROM SongTable")
    fun deleteAll()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'SongTable'")
    fun resetAutoIncrement()
}