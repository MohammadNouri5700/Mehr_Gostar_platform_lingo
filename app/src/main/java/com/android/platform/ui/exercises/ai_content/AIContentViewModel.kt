package com.android.platform.ui.exercises.ai_content

import android.os.Handler
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.ExerciseModel
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

class AIContentViewModel @Inject constructor(val call: CallQueueManager) : ViewModel() {


    private val _event = SingleLiveEvent<String>()
    val event: SingleLiveEvent<String> get() = _event



    lateinit var value: ExerciseModel
    val time = MutableLiveData<String>("00:00")
    private var timerJob: Int = -1
    private var recordStatus = false


    fun startVoice() {
        if (recordStatus) {
            _event.postValue("StopVoice")
            recordStatus = false
            stopTimer()
        } else {
            _event.postValue("StartVoice")
            recordStatus = true
            startTimer()
        }

    }

    fun startTimer() {
        var seconds = 0
        timerJob = call.enqueueIoTask {
            while (recordStatus) {
                delay(1000)
                seconds++
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                call.enqueueMainTask {
                    time.value = String.format(
                        "%02d:%02d",
                        minutes,
                        remainingSeconds
                    )
                }

            }
        }
    }

    fun stopTimer() {
        call.cancelTask(timerJob)

    }


}