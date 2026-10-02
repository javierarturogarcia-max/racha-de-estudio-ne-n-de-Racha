package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.StudyDayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDayDao {
    @Query("SELECT * FROM study_days ORDER BY dateIso DESC")
    fun getAllStudyDays(): Flow<List<StudyDayEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyDay(day: StudyDayEntity)

    @Query("DELETE FROM study_days WHERE dateIso = :dateIso")
    suspend fun deleteStudyDay(dateIso: String)

    @Query("SELECT EXISTS(SELECT 1 FROM study_days WHERE dateIso = :dateIso)")
    suspend fun hasStudiedOn(dateIso: String): Boolean

    @Query("DELETE FROM study_days")
    suspend fun clearAll()
}
