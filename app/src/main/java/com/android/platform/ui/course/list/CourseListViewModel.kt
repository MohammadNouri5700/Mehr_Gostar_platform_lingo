package com.android.platform.ui.course.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.LessonGrpc
import com.android.platform.LessonGrpc.LessonStub
import com.android.platform.LessonReply
import com.android.platform.LessonRequest
import com.android.platform.LessonsReply
import com.android.platform.LessonsRequest
import com.android.platform.LevelGrpc
import com.android.platform.RegisterReply
import com.android.platform.RegisterRequest
import com.android.platform.UserGrpcServiceGrpc
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.SingleLiveEvent
import javax.inject.Inject

class CourseListViewModel @Inject constructor():ViewModel() {

    @Inject
    lateinit var call: CallQueueManager

    @Inject
    lateinit var levelStub: LevelGrpc.LevelStub

    var lessonsReply: LessonsReply? = null

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

    fun loadList(value:Int){
        val request = LessonsRequest.newBuilder()
            .setLevelId(value)
            .build()

        levelStub.getLessons(request, object : io.grpc.stub.StreamObserver<LessonsReply> {
            override fun onNext(value: LessonsReply?) {
                val tst = value.toString()
                value?.let {
                    lessonsReply = it
                    _event.postValue("LoadList")
                }


            }

            override fun onError(t: Throwable?) {
                _event.postValue("Error")
            }

            override fun onCompleted() {

            }
        })
    }

    fun openCourse(value:Int){
        _selectedLessonId.value = value
    }

}