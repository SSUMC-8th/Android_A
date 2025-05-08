package com.example.floclone.database
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context


@Database(entities = [Song::class], version = 1)
abstract class SongDatabase : RoomDatabase(){

    abstract fun songDao(): SongDAO

    companion object {
        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getDatabase(context: Context): SongDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    "song_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        // 이렇게 코루틴 안에서 호출해야 함
        //lifecycleScope.launch {
        //    dao.insertSong(song)
        //}
    }

}