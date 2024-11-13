package com.android.platform.repository.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "exerciseLog")
data class ExerciseLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val status: String,
    val date: LocalDate,
    val time: LocalTime
)
