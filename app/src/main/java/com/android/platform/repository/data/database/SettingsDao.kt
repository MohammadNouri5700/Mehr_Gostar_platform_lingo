package com.android.platform.repository.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.platform.repository.data.model.ExerciseLogEntity
import com.android.platform.repository.data.model.NotificationSettingEntity
import com.android.platform.repository.data.model.SettingEntity
import com.android.platform.repository.data.model.UserLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: SettingEntity): Long

    @Update
    suspend fun update(value: SettingEntity)

    @Delete
    suspend fun delete(value: SettingEntity)

    @Query("SELECT * FROM settings")
    fun getAll(): Flow<List<SettingEntity>>

    @Query("SELECT * FROM settings WHERE id = :id")
    fun getById(id: Int): Flow<SettingEntity?>

    @Query("DELETE FROM settings")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM settings")
    fun count(): Flow<Int>
}