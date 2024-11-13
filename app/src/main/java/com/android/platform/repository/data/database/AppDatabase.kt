package com.android.platform.repository.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.platform.repository.data.model.ExerciseLogEntity
import com.android.platform.repository.data.model.ImageEntity
import com.android.platform.repository.data.model.NotificationSettingEntity
import com.android.platform.repository.data.model.SettingEntity
import com.android.platform.repository.data.model.UserLogEntity

@Database(entities = [UserLogEntity::class,ImageEntity::class,ExerciseLogEntity::class,NotificationSettingEntity::class,SettingEntity::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userLogDao(): UserLogDao
    abstract fun exerciseLogDaoDao(): ExerciseLogDao
    abstract fun imageDaoDao(): ImageDao
    abstract fun notificationDao(): NotificationDao
    abstract fun settingDao(): SettingsDao
}
