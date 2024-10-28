package com.android.platform.di.module

import android.content.Context
import androidx.room.Room
import com.android.platform.repository.data.database.AppDatabase
import com.android.platform.repository.data.database.ExerciseLogDao
import com.android.platform.repository.data.database.ImageDao
import com.android.platform.repository.data.database.NotificationDao
import com.android.platform.repository.data.database.SettingsDao
import com.android.platform.repository.data.database.UserLogDao


import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "lingoDB"
        ).build()
    }

    @Singleton
    @Provides
    fun provideUserLogDao(database: AppDatabase): UserLogDao {
        return database.userLogDao()
    }

    @Singleton
    @Provides
    fun provideExerciseLogDao(database: AppDatabase): ExerciseLogDao {
        return database.exerciseLogDaoDao()
    }

    @Singleton
    @Provides
    fun provideImageDao(database: AppDatabase): ImageDao {
        return database.imageDaoDao()
    }

    @Singleton
    @Provides
    fun provideSettingsDao(database: AppDatabase): SettingsDao {
        return database.settingDao()
    }

    @Singleton
    @Provides
    fun provideNotificationDao(database: AppDatabase): NotificationDao {
        return database.notificationDao()
    }

}