package com.android.platform.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.platform.data.local.dao.CourseDao
import com.android.platform.data.local.entity.Course

@Database(entities = [Course::class], version = 1, exportSchema = false)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}