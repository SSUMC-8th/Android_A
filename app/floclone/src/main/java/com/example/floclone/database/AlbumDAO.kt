package com.example.floclone.database
import androidx.room.*

@Dao
interface AlbumDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album)

    @Update
    suspend fun updateAlbum(album: Album)

    @Delete
    suspend fun deleteAlbum(album: Album)

    @Query("SELECT * FROM AlbumTable")
    suspend fun getAllAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    suspend fun getAlbumById(id: Int): Album?

    @Query("SELECT * FROM AlbumTable WHERE singer = :singer")
    suspend fun getAlbumsBySinger(singer: String): List<Album>

    @Query("UPDATE AlbumTable SET coverImg = :newRes WHERE coverImg = :oldRes")
    suspend fun updateCoverImg(oldRes: Int, newRes: Int)

    @Query("UPDATE AlbumTable SET coverImg = :newRes WHERE title = :titlename")
    suspend fun updateCoverImgByTitle(titlename: String, newRes: Int)

}