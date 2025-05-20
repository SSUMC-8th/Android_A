// SongDao.kt
package com.example.umc_8th

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song): Long // 삽입된 row id를 Long으로 반환하도록 변경

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable")
    fun getAllSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getLikedSongs(isLike: Boolean): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSongById(id: Int): Song? // ID로 노래를 가져오는 쿼리 추가

    @Query("SELECT * FROM SongTable WHERE title = :title AND singer = :artist LIMIT 1")
    fun getSongByTitleAndArtist(title: String, artist: String): Song?

    // !!! 이 부분이 새로 추가됩니다 !!!
    @Query("DELETE FROM SongTable")
    fun deleteAll() // 모든 노래를 삭제하는 쿼리
}