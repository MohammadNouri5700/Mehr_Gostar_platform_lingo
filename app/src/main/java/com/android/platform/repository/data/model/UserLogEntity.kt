package com.android.platform.repository.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "userLog")
data class UserLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val day: DayOfWeek
)
