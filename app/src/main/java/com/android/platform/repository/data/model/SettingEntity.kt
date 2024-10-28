package com.android.platform.repository.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "settings")
data class SettingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val language: LocalDate,
    val theme: LocalTime,
    val notification: NotificationSettingEntity
)
