package com.android.platform.ui.exercises.ai_voice

import android.content.Context
import android.media.AudioFormat
import android.media.MediaRecorder
import android.os.Environment
import java.io.File
import java.io.IOException
import java.util.UUID

class AudioRecorder(private val context: Context) {

    private val audioSource = MediaRecorder.AudioSource.MIC
    private val sampleRate = 44100 // 44.1kHz
    private var mediaRecorder: MediaRecorder? = null
    private val cacheDir: File = context.cacheDir
    private var isRecording = false

    // Start recording and save the file in AAC format
    fun startRecording(): String {
        if (isRecording) {
            throw IllegalStateException("Recording is already in progress")
        }

        val outputFilePath = createTempAacFile()
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(audioSource)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // MP4 container
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC) // AAC encoding
            setAudioSamplingRate(sampleRate) // 44.1kHz
            setOutputFile(outputFilePath)
        }


        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            isRecording = true
        } catch (e: IOException) {
            e.printStackTrace()
            throw IllegalStateException("Failed to start recording", e)
        }

        return outputFilePath
    }

    // Stops the recording process
    fun stopRecording() {
        if (!isRecording) {
            throw IllegalStateException("Recording has not been started or has already been stopped")
        }

        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            isRecording = false
        } catch (e: RuntimeException) {
            e.printStackTrace()
            throw IllegalStateException("Error while stopping the recording", e)
        }
    }

    // Creates a unique temp AAC file path in the cache directory
    private fun createTempAacFile(): String {
        val fileName = UUID.randomUUID().toString() + ".aac"
        val outputFilePath = File(cacheDir, fileName)
        return outputFilePath.absolutePath
    }
}