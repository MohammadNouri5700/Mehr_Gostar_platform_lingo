package com.android.platform.ui.exercises.ai_context

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.ExerciseModel
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.SingleLiveEvent
import javax.inject.Inject

class AIContextViewModel @Inject constructor(val call: CallQueueManager) : ViewModel() {



    private val _event = SingleLiveEvent<String>()
    val event: SingleLiveEvent<String> get() = _event



}