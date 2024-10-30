package com.android.platform.di.factory

import android.app.Application
import android.os.SystemClock
import android.util.Log
import com.android.platform.repository.data.database.UserLogDao
import com.android.platform.repository.data.model.UserLogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class UserSessionTracker(private val userLogDao: UserLogDao) {





    private var sessionStartTime: LocalTime? = null


    fun startSession() {
        sessionStartTime = LocalTime.now()
    }


    fun endSession() {
        val start = sessionStartTime ?: return
        val end = LocalTime.now()


        CoroutineScope(Dispatchers.IO).launch {
            val userLog = UserLogEntity(
                date = LocalDate.now(),
                startTime = start,
                endTime = end,
                day = LocalDate.now().dayOfWeek
            )
            userLogDao.insert(userLog)
        }

        sessionStartTime = null
    }
}