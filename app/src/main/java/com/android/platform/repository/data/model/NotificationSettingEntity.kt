package com.android.platform.repository.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "notificationSetting")
data class NotificationSettingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val all: Boolean = true,
    val stories: Boolean = false,
    val comments: Boolean = false,
    val challenge: Boolean = false,
    val general: Boolean = false
)

