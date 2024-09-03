package com.android.platform.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Level(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val levelName: String,
    val levelDuration: String,
    val levelProgress: Int
)