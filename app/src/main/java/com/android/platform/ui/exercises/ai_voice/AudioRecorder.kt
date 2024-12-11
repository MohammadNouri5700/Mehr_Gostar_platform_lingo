package com.android.platform.ui.exercises.ai_voice

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.google.protobuf.ByteString
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.UUID

class AudioRecorder(context: Context) {

    private val audioSource = MediaRecorder.AudioSource.MIC
    private val sampleRate = 44800
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = 512
    private val audioRecord: AudioRecord = AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, bufferSize)
    private val cacheDir: File = context.cacheDir

    // Start recording and save the WAV file
    fun startRecording(): String {
        val outputFilePath = createTempWavFile()
        val fileOutputStream = FileOutputStream(outputFilePath)
        val byteBuffer = ByteBuffer.allocateDirect(bufferSize)

        Thread {
            try {
                audioRecord.startRecording()
                while (audioRecord.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    val readBytes = audioRecord.read(byteBuffer, bufferSize)
                    if (readBytes > 0) {
                        val bytes = ByteArray(readBytes)
                        byteBuffer.get(bytes)
                        fileOutputStream.write(bytes)
                        byteBuffer.clear()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                audioRecord.stop()
                fileOutputStream.close()
            }
        }.start()

        return outputFilePath
    }

    // Stops the recording process
    fun stopRecording() {
        audioRecord.stop()
    }

    // Creates a unique temp WAV file path in the cache directory
    private fun createTempWavFile(): String {
        val fileName = UUID.randomUUID().toString() + ".wav"
        val outputFilePath = File(cacheDir, fileName)
        return outputFilePath.absolutePath
    }

    fun convertWavFileToByteString(filePath: String): ByteString {
        val file = File(filePath)
        val byteArray = ByteArray(file.length().toInt())

        try {
            FileInputStream(file).use { inputStream ->
                inputStream.read(byteArray)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ByteString.copyFrom(byteArray)
    }
}
