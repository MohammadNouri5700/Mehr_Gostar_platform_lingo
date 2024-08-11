package com.android.platform.di.module

import android.content.Context
import androidx.room.Room
import com.android.platform.data.local.CourseDatabase
import com.android.platform.data.local.dao.CourseDao

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): CourseDatabase {
        return Room.databaseBuilder(context, CourseDatabase::class.java, "course_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCourseDao(database: CourseDatabase): CourseDao {
        return database.courseDao()
    }
}