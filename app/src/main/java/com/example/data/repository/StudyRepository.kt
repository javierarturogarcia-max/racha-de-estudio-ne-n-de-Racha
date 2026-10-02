package com.example.data.repository

import com.example.data.db.StudyDayDao
import com.example.data.model.StudyDayEntity
import com.example.util.DateUtils
import kotlinx.coroutines.flow.Flow

class StudyRepository(private val dao: StudyDayDao) {
    val allStudyDays: Flow<List<StudyDayEntity>> = dao.getAllStudyDays()

    suspend fun recordStudyToday(): Boolean {
        val todayIso = DateUtils.getTodayIso()
        dao.insertStudyDay(StudyDayEntity(dateIso = todayIso, timestamp = System.currentTimeMillis()))
        return true
    }

    suspend fun toggleStudyDay(dateIso: String) {
        val exists = dao.hasStudiedOn(dateIso)
        if (exists) {
            dao.deleteStudyDay(dateIso)
        } else {
            dao.insertStudyDay(StudyDayEntity(dateIso = dateIso, timestamp = System.currentTimeMillis()))
        }
    }

    suspend fun removeStudyToday() {
        val todayIso = DateUtils.getTodayIso()
        dao.deleteStudyDay(todayIso)
    }

    suspend fun hasStudiedToday(): Boolean {
        return dao.hasStudiedOn(DateUtils.getTodayIso())
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
