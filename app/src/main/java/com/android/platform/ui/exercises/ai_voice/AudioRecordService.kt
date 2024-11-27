package com.android.platform.ui.exercises.ai_voice
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.media.MediaRecorder
import java.io.IOException

class AudioRecordService : Service() {
    private val binder = LocalBinder()
    private var recorder: MediaRecorder? = null

    inner class LocalBinder : Binder() {
        fun getService(): AudioRecordService = this@AudioRecordService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun startRecording(filePath: String) {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(filePath)
            try {
                prepare()
                start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }
}
