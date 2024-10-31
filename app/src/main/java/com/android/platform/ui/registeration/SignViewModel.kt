package com.android.platform.ui.registeration

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.platform.LoginReply
import com.android.platform.LoginRequest
import com.android.platform.RegisterReply
import com.android.platform.RegisterRequest
import com.android.platform.UserGrpcServiceGrpc
import com.android.platform.repository.data.database.UserLogDao
import com.android.platform.utils.extension.getLastSevenDays
import com.android.platform.utils.extension.getPercentageOfDay
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

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

    suspend fun registerUser() {
        val fcm = getFCMToken()
        val request = RegisterRequest.newBuilder()
            .setMacAddress("189984")
            .setPhoneNumber("09386184857")
            .setFcmToken(fcm)
            .build()

        var token = ""
        greeterStub.register(request, object : io.grpc.stub.StreamObserver<RegisterReply> {
            override fun onNext(value: RegisterReply?) {
                value?.let {
//                    Activity?.showToast(it.token)
                    token = it.token
                    Log.e("APP", "TOKEN == ${it.token}")
                }
            }

            override fun onError(t: Throwable?) {
                Log.e("APP", "we err ${t?.message}")
                t?.printStackTrace()

            }

            override fun onCompleted() {
                Log.e("APP", "we err Completed")
            }
        })
    }


}