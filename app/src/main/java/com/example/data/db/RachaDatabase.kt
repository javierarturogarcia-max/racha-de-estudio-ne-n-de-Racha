package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.StudyDayEntity

@Database(entities = [StudyDayEntity::class], version = 1, exportSchema = false)
abstract class RachaDatabase : RoomDatabase() {
    abstract fun studyDayDao(): StudyDayDao

    companion object {
        @Volatile
        private var INSTANCE: RachaDatabase? = null

        fun getInstance(context: Context): RachaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RachaDatabase::class.java,
                    "racha_study_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
