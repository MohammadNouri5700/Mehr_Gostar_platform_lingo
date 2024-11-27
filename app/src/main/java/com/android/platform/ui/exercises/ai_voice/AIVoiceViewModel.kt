package com.android.platform.ui.exercises.ai_voice

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.platform.di.factory.SingleLiveEvent
import com.android.platform.repository.data.model.BotMessageEntity
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AIVoiceViewModel @Inject constructor() : ViewModel() {


    var messageList: ArrayList<BotMessageEntity> = arrayListOf()

    private val _event = SingleLiveEvent<String>()
    val event: SingleLiveEvent<String> get() = _event


    fun startListening(context: Context) {


        try{
            Speech.getInstance().setPreferOffline(false)
            Speech.getInstance().setLocale(Locale.GERMAN)
        } catch (ex: Exception){
            Log.e("SpeechError", "Language not supported!")
        }

        Speech.getInstance().startListening(object : SpeechDelegate {
            override fun onStartOfSpeech() {
                Log.e("APP", "onStartOfSpeech")

            }

            override fun onSpeechRmsChanged(value: Float) {
                Log.e("APP", "onSpeechRmsChanged $value")

            }

            override fun onSpeechPartialResults(results: MutableList<String>?) {
                results?.let {
                    Log.e("APP", "onSpeechPartialResults ${it.joinToString(", ")}")
                }
            }

            override fun onSpeechResult(result: String?) {
                Log.e("APP", "RES = $result")
                if (result != null&& !result.isEmpty())
                    _event.postValue(result!!)
            }
        })
    }


    fun stopListening() {
        Speech.getInstance().stopListening()
    }


}