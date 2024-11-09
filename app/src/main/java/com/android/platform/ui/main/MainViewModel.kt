package com.android.platform.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.android.platform.RegisterReply
import com.android.platform.RegisterRequest
import com.android.platform.PlatformApplication
import com.android.platform.UserGrpcServiceGrpc
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.Preferences
import com.android.platform.repository.data.database.UserLogDao
import com.android.platform.utils.extension.getAndroidId
import com.android.platform.utils.extension.getFCMToken
import com.android.platform.utils.extension.showToast
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var userLogDao: UserLogDao

    @Inject
    lateinit var preferences: Preferences

    @Inject
    lateinit var call: CallQueueManager

    @Inject
    lateinit var greeterStub: UserGrpcServiceGrpc.UserGrpcServiceStub

    private val _event = MutableLiveData<String>().apply {
        value = "Loading"
    }
    val event: LiveData<String> = _event


    fun openHome() {
        _event.postValue("Home")
    }

    fun openLearn() {
        _event.postValue("Learn")
    }

    fun openReport() {
        _event.postValue("Report")
    }

    fun openProfile() {
        _event.postValue("Profile")
    }

    fun viewGetToken(context: Context){
        call.enqueueIoTask {
            getToken(context)
        }
    }

    private fun getToken(context: Context) {
        call.enqueueIoTask {
            if (preferences.getString("PHONE")==null||preferences.getString("PHONE")==""){
                _event.postValue("ErrorLogin")
                return@enqueueIoTask
            }
            val fcm = this@MainViewModel.getFCMToken()
            val request = RegisterRequest.newBuilder()
                .setMacAddress(getAndroidId(context))
                .setPhoneNumber(preferences.getString("PHONE"))
                .setFcmToken(fcm)
                .build()
            greeterStub.register(request, object : io.grpc.stub.StreamObserver<RegisterReply> {
                override fun onNext(value: RegisterReply?) {
                    value?.let {
                        Log.e("APP", "TOKEN == ${it.token}")
                        preferences.putString("TOKEN",it.token)
                        _event.postValue("Init")
                    }
                }

                override fun onError(t: Throwable?) {
                    _event.postValue("ErrorLogin")
                }

                override fun onCompleted() {
                }
            })
        }
    }

    suspend fun getTest():String {
        val (minutes, _) = withContext(Dispatchers.IO) {
            userLogDao.getDurationForDateCompleted(LocalDate.now())
        }
        return minutes.toString()
    }

    fun openFirst() {
        openLearn()
    }

}