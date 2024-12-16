package com.android.platform.ui.exercises.ai_voice

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.platform.RecognitionConfig
import com.android.platform.RecognitionSpec
import com.android.platform.RegisterRequest
import com.android.platform.StreamingRecognitionRequest
import com.android.platform.StreamingRecognitionResponse
import com.android.platform.SttServiceGrpc
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.SingleLiveEvent
import com.android.platform.di.module.GrpcModule.ChannelAI
import com.android.platform.repository.data.model.BotMessageEntity

import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.concurrent.thread

class AIVoiceViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var call: CallQueueManager


    @Inject
    @ChannelAI
    lateinit var aiVoice: SttServiceGrpc.SttServiceStub

    var messageList: ArrayList<BotMessageEntity> = arrayListOf()

    private val _event = SingleLiveEvent<String>()
    val event: SingleLiveEvent<String> get() = _event


    fun startListeningGoogle(context: Context) {
        try {
            Speech.getInstance().setPreferOffline(false)
        } catch (ex: Exception) {
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
                if (result != null && !result.isEmpty()) _event.postValue(result!!)
            }
        })
    }


    fun stopListeningGoogle() {
        Speech.getInstance().stopListening()
    }


    fun sendVoice(ctx: Context, data: File) {

        call.enqueueIoTask {
            try {
                val audioFile = File(data.absolutePath)
                if (!audioFile.exists()) {
                    Log.e("WebSocket", "Audio file not found: $audioFile")
                    return@enqueueIoTask
                }

                val fileInputStream = FileInputStream(audioFile)
                val fileChannel: FileChannel = fileInputStream.channel
                val buffer = ByteBuffer.allocate(512)

                // WebSocket Client
                val client = OkHttpClient()
                val request = Request.Builder().url("ws://176.120.16.242:2700").build()

                val webSocketListener = object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {

                    }

                    override fun onMessage(webSocket: WebSocket, text: String) {
                        Log.d("WebSocket", "Received: $text")
                        call.enqueueMainTask {
                            val json = JSONObject(text)
                            if (json.has("alternatives")) {
                                val alternatives = json.getJSONArray("alternatives")


                                var maxConfidence = Double.MIN_VALUE
                                var bestText = ""


                                for (i in 0 until alternatives.length()) {
                                    val alternative = alternatives.getJSONObject(i)
                                    val confidence = alternative.getDouble("confidence")
                                    val text = alternative.getString("text")


                                    if (confidence > maxConfidence) {
                                        maxConfidence = confidence
                                        bestText = text
                                    }
                                }

                                if (maxConfidence > 300){
                                    _event.postValue("Confidence > 300 is $maxConfidence")
                                }else{
                                    bestText+= "      Confidence : $maxConfidence"
                                    _event.postValue(bestText)
                                }

                            }
                        }
                    }

                    override fun onFailure(
                        webSocket: WebSocket, t: Throwable, response: Response?
                    ) {
                        Log.e("WebSocket", "Error: ${t.message}")
                    }
                }

                val config = JSONObject().apply {
                    put("config", JSONObject().apply {
//                        put("phrase_list", JSONArray().apply {
//                            put("As you know, we have been working on the new perfume that we are launching in April and we are unsure about some of the packaging details. We have seen some of your creative work in the sales department and we think you have a very good eye for detail.")
//                            put("Do you have some time before close of business this Friday to sit down with us and talk through some of our designs? We would truly appreciate your advice on this. It shouldn't take longer than two hours of your time and we would be happy to clear it with Patricia, your department head, if necessary.")
//                        })
                        put("sample_rate", 44800)
                        put("words", 0)
                        put("max_alternatives",10)
                    })
                }



                val webSocket = client.newWebSocket(request, webSocketListener)

                webSocket.send(config.toString())
                Log.d("WebSocket", "Sent config: $config")


                while (fileChannel.read(buffer) != -1) {
                    buffer.flip()
                    val byteArray = ByteArray(buffer.remaining())
                    buffer.get(byteArray)

                    val byteString = okio.ByteString.of(*byteArray)
                    webSocket.send(byteString)
                    buffer.clear()
                    Thread.sleep(0)
                }

                webSocket.send("{\"eof\": 1}")
                Log.d("WebSocket", "Sent EOF message")

                fileInputStream.close()
//                webSocket.close(1000,"NULL")
            } catch (e: IOException) {
                Log.e("WebSocket", "Error reading audio file: ${e.message}")
            }
        }
    }


}