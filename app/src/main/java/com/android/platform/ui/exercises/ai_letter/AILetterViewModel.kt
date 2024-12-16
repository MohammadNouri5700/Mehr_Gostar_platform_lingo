package com.android.platform.ui.exercises.ai_letter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.ExerciseModel
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.SingleLiveEvent
import javax.inject.Inject

class AILetterViewModel @Inject constructor(val call: CallQueueManager) :  ViewModel() {


    private val _event = SingleLiveEvent<String>()
    val event: SingleLiveEvent<String> get() = _event
    lateinit var value: ExerciseModel

    val wordCount = MutableLiveData<String>("0")
    val essay = MutableLiveData<String>("")


    fun confirm(){
        _event.postValue("Confirm")
    }


    fun setCount(){
        val words = essay.value.toString().split("\\s+".toRegex()).filter { it.isNotEmpty() }
        wordCount.value = words.size.toString()
    }




}