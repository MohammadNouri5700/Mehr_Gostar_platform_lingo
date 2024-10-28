package com.android.platform.repository.data.database

import androidx.room.TypeConverter
import com.android.platform.repository.data.model.NotificationSettingEntity
import com.google.gson.Gson
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Converters {

    private val customDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    private val customTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.format(customDateFormatter)

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate = LocalDate.parse(dateString, customDateFormatter)

    @TypeConverter
    fun fromLocalTime(time: LocalTime): String = time.format(customTimeFormatter)

    @TypeConverter
    fun toLocalTime(timeString: String): LocalTime = LocalTime.parse(timeString, customTimeFormatter)



    @TypeConverter
    fun fromNotificationSettingEntity(value: NotificationSettingEntity?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toNotificationSettingEntity(value: String?): NotificationSettingEntity? {
        return Gson().fromJson(value, NotificationSettingEntity::class.java)
    }

}