package com.android.platform.ui.level

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.platform.HomeGrpc
import com.android.platform.LevelGrpc
import com.android.platform.LevelsReply
import com.android.platform.LevelsRequest
import com.android.platform.PodcastCategoryReply
import com.android.platform.PodcastCategoryRequest
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import javax.inject.Inject

class LevelViewModel @Inject constructor(
    val Stub: LevelGrpc.LevelStub,
    val call: CallQueueManager
) : ViewModel() {


    var levelsReply: LevelsReply? = null
    private val _event = MutableLiveData<String>()
    val event: LiveData<String> get() = _event
    private val _selectedLevelId = SingleLiveEvent<Int>()
    val selectedLevelId: LiveData<Int> get() = _selectedLevelId

    init {
        _event.value = "Loading"
        call.enqueueIoTask {
            getLevels()
        }
    }


    fun onLevelClicked(levelId: Int) {
        _selectedLevelId.value = levelId
    }

    private fun getLevels() {
        val request = LevelsRequest.newBuilder()
            .build()
        Stub.getLevels(request, object : io.grpc.stub.StreamObserver<LevelsReply> {
            override fun onNext(value: LevelsReply?) {
                levelsReply = value
                _event.postValue("LevelsUpdated")
            }

            override fun onError(t: Throwable?) {
                Log.e("APP", "we err ${t?.message}")
                t?.printStackTrace()

            }

            override fun onCompleted() {
                Log.e("APP", "onCompleted getLevels")
            }
        })
    }


}