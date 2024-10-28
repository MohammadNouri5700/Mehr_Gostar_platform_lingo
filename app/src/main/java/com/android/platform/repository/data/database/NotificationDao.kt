package com.android.platform.repository.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.platform.repository.data.model.ExerciseLogEntity
import com.android.platform.repository.data.model.NotificationSettingEntity
import com.android.platform.repository.data.model.UserLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: NotificationSettingEntity): Long

    @Update
    suspend fun update(value: NotificationSettingEntity)

    @Delete
    suspend fun delete(value: NotificationSettingEntity)

    @Query("SELECT * FROM notificationSetting")
    fun getAll(): Flow<List<NotificationSettingEntity>>

    @Query("SELECT * FROM notificationSetting WHERE id = :id")
    fun getById(id: Int): Flow<NotificationSettingEntity?>

    @Query("DELETE FROM notificationSetting")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM notificationSetting")
    fun count(): Flow<Int>
}