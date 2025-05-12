package com.example.umc_8th.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.umc_8th.dao.AlbumDao
import com.example.umc_8th.entity.AlbumEntity
import com.example.umc_8th.entity.SongEntity
import com.example.umc_8th.dao.SongDao

@Database(entities = [SongEntity::class, AlbumEntity::class], version = 2)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao

    companion object {
        @Volatile private var INSTANCE: SongDatabase? = null

        fun getDatabase(context: Context): SongDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    "song_database"
                )
                    //.fallbackToDestructiveMigration()  // 기존 데이터베이스 삭제 후 재생성
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
