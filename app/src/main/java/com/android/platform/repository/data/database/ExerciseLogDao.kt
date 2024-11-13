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
interface ExerciseLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: ExerciseLogEntity): Long

    @Update
    suspend fun update(value: ExerciseLogEntity)

    @Delete
    suspend fun delete(value: ExerciseLogEntity)

    @Query("SELECT * FROM exerciseLog")
    fun getAll(): Flow<List<ExerciseLogEntity>>

    @Query("SELECT * FROM exerciseLog WHERE id = :id")
    fun getById(id: Int): Flow<ExerciseLogEntity?>

    @Query("DELETE FROM exerciseLog")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM exerciseLog")
    fun count(): Flow<Int>
}