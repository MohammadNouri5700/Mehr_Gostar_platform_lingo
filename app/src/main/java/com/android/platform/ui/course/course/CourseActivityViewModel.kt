package com.android.platform.ui.course.course

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.LessonGrpc
import com.android.platform.LessonReply
import com.android.platform.LessonRequest
import com.android.platform.LevelGrpc
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.SingleLiveEvent
import com.android.platform.ui.course.course.ui.SelectCourseItemDialog

import javax.inject.Inject

class CourseActivityViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var call: CallQueueManager

    @Inject
    lateinit var lessonStub: LessonGrpc.LessonStub

    var lessonReply: LessonReply? =null


    private val _selectedLessonId = SingleLiveEvent<Int>()
    val selectedLessonId: LiveData<Int> get() = _selectedLessonId
    private val _event = MutableLiveData<String>().apply {
        value = "Loading"
    }
    val event: LiveData<String> = _event


    init {
        _event.postValue("Loading")
    }


    fun back(){
        _event.postValue("Back")
    }

    fun loadLesson(value:Int){
        val request = LessonRequest.newBuilder()
            .setLessonId(value)
            .build()

        lessonStub.getLesson(request, object : io.grpc.stub.StreamObserver<LessonReply> {
            override fun onNext(value: LessonReply?) {
                value?.let {
                    lessonReply = it
                    _event.postValue("LoadLesson")
                }


            }

            override fun onError(t: Throwable?) {
                _event.postValue("Error")
            }

            override fun onCompleted() {

            }
        })
    }

    fun loadItem(value: Int){
        _selectedLessonId.value = value
    }
}