package com.android.platform.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.LessonReply
import com.android.platform.di.factory.SingleLiveEvent
import javax.inject.Inject

class ExerciseViewModel @Inject constructor(): ViewModel() {


    lateinit var lessonReply: LessonReply
    var exerciseId: Int = -1

    private val _event = SingleLiveEvent<String>()
    val event: LiveData<String> get() = _event


    init {
        _event.postValue("HideNav")
    }

    fun confirmExercise(){
        _event.postValue("ConfirmExercise")
    }
    fun closeExercise(){
        _event.postValue("Close")
    }


    fun showNavBars(){
        _event.postValue("ShowNav")
    }
    fun hideNavBars(){
        _event.postValue("HideNav")
    }
}