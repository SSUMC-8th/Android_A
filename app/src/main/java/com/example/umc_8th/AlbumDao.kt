// AlbumDao.kt
package com.example.umc_8th

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update

@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album): Long

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAllAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbumById(id: Int): Album?

    @Query("SELECT * FROM AlbumTable WHERE title = :title AND singer = :singer LIMIT 1")
    fun getAlbumByTitleAndSinger(title: String, singer: String): Album?

    @Query("DELETE FROM AlbumTable")
    fun deleteAllAlbums() // 모든 앨범을 삭제하는 쿼리
}