package com.android.platform.ui.registeration.old

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.RegisterReply
import com.android.platform.RegisterRequest
import com.android.platform.UserGrpcServiceGrpc
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SignViewModel @Inject constructor() : ViewModel() {


    @Inject
    lateinit var greeterStub: UserGrpcServiceGrpc.UserGrpcServiceStub


    private val _event = MutableLiveData<String>()
    val event: LiveData<String> = _event


    init {
        _event.value = "Hello from ViewModel!"

    }


    private suspend fun getFCMToken(): String? = withContext(Dispatchers.IO) {
        try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            Log.w("TAG", "Fetching FCM registration token failed", e)
            "Fetching FCM registration token failed $e"
        }
    }

    suspend fun registerUser(value:String,deviceToken:String) {



        val fcm = getFCMToken()
        val request = RegisterRequest.newBuilder()
            .setMacAddress(deviceToken)
            .setPhoneNumber(value)
            .setFcmToken(fcm)
            .build()

        var token = ""
        greeterStub.register(request, object : io.grpc.stub.StreamObserver<RegisterReply> {
            override fun onNext(value: RegisterReply?) {
                value?.let {
                    Log.e("APP", "TOKEN == ${it.token}")
                }
            }

            override fun onError(t: Throwable?) {
                t?.printStackTrace()

            }

            override fun onCompleted() {
            }
        })
    }


}