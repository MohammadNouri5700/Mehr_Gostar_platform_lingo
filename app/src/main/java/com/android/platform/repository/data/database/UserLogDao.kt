package com.android.platform.repository.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.platform.repository.data.model.ExerciseLogEntity
import com.android.platform.repository.data.model.UserLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: UserLogEntity): Long

    @Update
    suspend fun update(value: UserLogEntity)

    @Delete
    suspend fun delete(value: UserLogEntity)

    @Query("SELECT * FROM userLog")
    fun getAll(): Flow<List<UserLogEntity>>

    @Query("SELECT * FROM userLog WHERE id = :id")
    fun getById(id: Int): Flow<UserLogEntity?>

    @Query("DELETE FROM userLog")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM userLog")
    fun count(): Flow<Int>
}