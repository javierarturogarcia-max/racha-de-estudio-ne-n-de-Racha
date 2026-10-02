package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_days")
data class StudyDayEntity(
    @PrimaryKey
    val dateIso: String, // format "YYYY-MM-DD"
    val timestamp: Long = System.currentTimeMillis()
)
