package com.android.platform.repository.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.platform.repository.data.model.ImageEntity
import com.android.platform.repository.data.model.NotificationSettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query("SELECT * FROM images WHERE imageUrl = :url LIMIT 1")
    suspend fun getImageByUrl(url: String): ImageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: ImageEntity): Long

    @Update
    suspend fun update(value: ImageEntity)

    @Delete
    suspend fun delete(value: ImageEntity)

    @Query("SELECT * FROM images")
    fun getAll(): Flow<List<ImageEntity>>

    @Query("SELECT * FROM images WHERE id = :id")
    fun getById(id: Int): Flow<ImageEntity?>

    @Query("DELETE FROM images")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM images")
    fun count(): Flow<Int>

}