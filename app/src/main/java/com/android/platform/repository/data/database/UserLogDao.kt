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
import java.time.Duration
import java.time.LocalDate

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

    @Query("SELECT * FROM userLog WHERE date = :date")
    suspend fun getUserLogsByDate(date: LocalDate): List<UserLogEntity>

    suspend fun calculateDurationForDate(date: LocalDate): Long {
        val logs = getUserLogsByDate(date)

        return logs.sumOf { log ->
            Duration.between(log.startTime, log.endTime).toMinutes()
        }
    }

    suspend fun getDurationForDateCompleted(date: LocalDate): Pair<Long, Long> {
        val logs = getUserLogsByDate(date)

        val totalSeconds = logs.sumOf { log ->
            Duration.between(log.startTime, log.endTime).seconds
        }

        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        return Pair(minutes, seconds)
    }
}