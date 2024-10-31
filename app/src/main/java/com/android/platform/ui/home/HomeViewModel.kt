package com.android.platform.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.platform.HomeGrpc
import com.android.platform.PodcastCategory
import com.android.platform.PodcastCategoryReply
import com.android.platform.PodcastCategoryRequest
import com.android.platform.RegisterReply
import com.android.platform.RegisterRequest
import com.android.platform.StorisReply
import com.android.platform.StorisRequest
import com.android.platform.UserGrpcServiceGrpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import javax.inject.Inject

class HomeViewModel @Inject constructor(val Stub: HomeGrpc.HomeStub) : ViewModel() {

    var stories: StorisReply? = null
    var podcastCategory: PodcastCategoryReply? = null




    private val _event = MutableLiveData<String>()
    val event: LiveData<String> get() = _event

    init {
        _event.value = "Loading"
        viewModelScope.launch {
           withContext(Dispatchers.IO){
                getStory()
            }
            withContext(Dispatchers.IO){
                getPodcastCategory()
            }
        }
    }


    private fun getStory() {
        val request = StorisRequest.newBuilder()
            .setCount(10)
            .setPage(1)
            .build()
        Stub.getStoris(request, object : io.grpc.stub.StreamObserver<StorisReply> {
            override fun onNext(value: StorisReply?) {
                stories=value
                _event.postValue("StoryUpdate")
            }

            override fun onError(t: Throwable?) {
                Log.e("APP", "we err ${t?.message}")
                t?.printStackTrace()

            }

            override fun onCompleted() {
                Log.e("APP", "onCompleted getStories")
            }
        })
    }

    private fun getPodcastCategory() {
        val request = PodcastCategoryRequest.newBuilder()
            .build()
        Stub.getPodcastCategory(request, object : io.grpc.stub.StreamObserver<PodcastCategoryReply> {
            override fun onNext(value: PodcastCategoryReply?) {
                podcastCategory=value
                _event.postValue("PodcastCategoryUpdate")
            }

            override fun onError(t: Throwable?) {
                Log.e("APP", "we err ${t?.message}")
                t?.printStackTrace()

            }

            override fun onCompleted() {
                Log.e("APP", "onCompleted PodcastCategoryReply")
            }
        })
    }


}