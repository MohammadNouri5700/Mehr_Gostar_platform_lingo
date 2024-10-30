package com.android.platform.repository.data.database

import androidx.room.TypeConverter
import com.android.platform.repository.data.model.NotificationSettingEntity
import com.google.gson.Gson
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Converters {

    private val customDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    private val customTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    private val gson = Gson()

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.format(customDateFormatter)
    @TypeConverter
    fun fromLocalTime(time: LocalTime): String = time.format(customTimeFormatter)

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, customDateFormatter) }
    }
    @TypeConverter
    fun toLocalTime(timeString: String?): LocalTime? {
        return timeString?.let { LocalTime.parse(it, customTimeFormatter) }
    }

    @TypeConverter
    fun fromDayOfWeek(day: DayOfWeek): String = day.name

    @TypeConverter
    fun toDayOfWeek(dayString: String): DayOfWeek = DayOfWeek.valueOf(dayString)


    @TypeConverter
    fun fromNotificationSettingEntity(value: NotificationSettingEntity?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toNotificationSettingEntity(value: String?): NotificationSettingEntity? {
        return gson.fromJson(value, NotificationSettingEntity::class.java)
    }

}