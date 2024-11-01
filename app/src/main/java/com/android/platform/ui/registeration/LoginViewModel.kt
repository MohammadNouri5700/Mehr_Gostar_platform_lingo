package com.android.platform.ui.registeration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.RegisterReply
import com.android.platform.RegisterRequest
import com.android.platform.UserGrpcServiceGrpc
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.LoadingDialog
import com.android.platform.di.factory.Preferences
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var call: CallQueueManager

    @Inject
    lateinit var preferences: Preferences

    @Inject
    lateinit var loadingDialog: LoadingDialog

    @Inject
    lateinit var greeterStub: UserGrpcServiceGrpc.UserGrpcServiceStub


    private val _event = MutableLiveData<String>()
    val event: LiveData<String> = _event


    init {
        _event.value = "Init"

    }
    fun isRegistered():Boolean{
        return preferences.getString("TOKEN")!=null
    }


    private suspend fun getFCMToken(): String = withContext(Dispatchers.IO) {
        try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
           "FCM registration token failed $e"
        }
    }

    suspend fun registerUser(phone:String,deviceToken:String) {
        _event.postValue("Loading")

        val fcm = getFCMToken()
        val request = RegisterRequest.newBuilder()
            .setMacAddress(deviceToken)
            .setPhoneNumber(phone)
            .setFcmToken(fcm)
            .build()

        greeterStub.register(request, object : io.grpc.stub.StreamObserver<RegisterReply> {
            override fun onNext(value: RegisterReply?) {
                value?.let {
                    preferences.putString("TOKEN",value.token)
                    preferences.putString("PHONE",phone)
                    _event.postValue("Login")
                }

            }

            override fun onError(t: Throwable?) {
                _event.postValue("Error")
            }

            override fun onCompleted() {

            }
        })
    }



}