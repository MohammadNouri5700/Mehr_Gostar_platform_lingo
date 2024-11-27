package com.android.platform.ui.exercises.ai_voice


import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import java.io.BufferedOutputStream
import java.io.DataOutputStream
import java.io.FileOutputStream
import java.io.IOException

class AudioRecorderThread(context: Context, private val fileName: String) : Thread() {
    private var isRecording = false
    private val sampleRate = 44100 // Hz
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    private val filePath = "${context.cacheDir.absolutePath}/$fileName"

    override fun run() {
        val audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, minBufferSize)
        val audioData = ShortArray(minBufferSize)
        val outputStream = createFileOutputStream(filePath)

        audioRecord.startRecording()
        isRecording = true

        try {
            while (isRecording) {
                val readSize = audioRecord.read(audioData, 0, minBufferSize)
                // Convert short to byte and write to file
                for (i in 0 until readSize) {
                    outputStream.writeShort(audioData[i].toInt())
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                audioRecord.stop()
                audioRecord.release()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun createFileOutputStream(filePath: String): DataOutputStream {
        val fos = FileOutputStream(filePath)
        val bos = BufferedOutputStream(fos)
        return DataOutputStream(bos)
    }

    fun stopRecording() {
        isRecording = false
    }
}
