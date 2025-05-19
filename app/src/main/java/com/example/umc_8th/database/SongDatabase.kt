package com.example.umc_8th.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.umc_8th.dao.AlbumDao
import com.example.umc_8th.dao.SongDao
import com.example.umc_8th.entity.AlbumEntity
import com.example.umc_8th.entity.SongEntity

@Database(entities = [SongEntity::class, AlbumEntity::class], version = 3)
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
                    //.fallbackToDestructiveMigration() // 필요 시 주석 해제
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
