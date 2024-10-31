package com.android.platform.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.repository.data.database.UserLogDao
import com.android.platform.utils.extension.getLastSevenDays
import com.android.platform.utils.extension.getPercentageOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class ReportViewModel  @Inject constructor(val userLogDao: UserLogDao,val call: CallQueueManager) : ViewModel() {


    private val _event = MutableLiveData<String>()
    private val _liveDayReport = MutableLiveData<Triple<Float, String,Int>>()
    val liveDayReport: LiveData<Triple<Float, String,Int>> = _liveDayReport
    val event: LiveData<String> = _event

    private val lastSevenDays = getLastSevenDays()

    init {
        _event.value = "Hello from ViewModel!"
    }

    fun getReportDayOftWeek(index:Int){
        viewModelScope.launch {
            val (minutes, seconds) = withContext(Dispatchers.IO) {
                userLogDao.getDurationForDateCompleted(LocalDate.now().minusDays(index.toLong()))
            }
            _liveDayReport.postValue(Triple<Float, String,Int>(getPercentageOfDay(minutes), lastSevenDays[index],index))

        }
    }

    suspend fun getReportTotalWeek():String = userLogDao.calculateDurationForLastWeek()





}