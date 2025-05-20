package com.example.umc_8th

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao

    companion object {
        @Volatile // 메인 메모리에 즉시 반영되도록 보장
        private var INSTANCE: SongDatabase? = null

        fun getInstance(context: Context): SongDatabase {
            // INSTANCE가 null이면 동기화 블록으로 진입하여 데이터베이스 생성
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, // 애플리케이션 컨텍스트 사용
                    SongDatabase::class.java,
                    "song-database" // 데이터베이스 파일 이름
                )
                    // .fallbackToDestructiveMigration() // 버전 업그레이드 시 데이터 손실 감수하고 스키마 재생성
                    .allowMainThreadQueries() // 메인 스레드에서 쿼리 허용 (테스트 및 간단한 앱에 유용, 권장X)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // 데이터베이스 초기화 함수 (필요시 사용)
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
