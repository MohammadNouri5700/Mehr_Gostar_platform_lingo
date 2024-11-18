package com.android.platform.ui.exercises.listening

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.platform.ExerciseModel
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentListeningExerciseBinding
import com.masoudss.lib.SeekBarOnProgressChanged
import com.masoudss.lib.WaveformSeekBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ListeningFragment @Inject constructor(val value: ExerciseModel) : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ListeningViewModel
    private lateinit var binding: FragmentListeningExerciseBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_listening_exercise,
            container,
            false
        )
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[ListeningViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaPlayer = MediaPlayer()

        // دانلود فایل صوتی و آماده کردن MediaPlayer
        downloadAudioFile("https://dl.lingomars.ir/general/sway.mp3")

        binding.wave.onProgressChanged = object : SeekBarOnProgressChanged {
            override fun onProgressChanged(
                waveformSeekBar: WaveformSeekBar,
                progress: Float,
                fromUser: Boolean
            ) {
                if (fromUser && mediaPlayer.isPlaying) {
                    mediaPlayer.seekTo(progress.toInt())
                }
            }
        }

        binding.imgControl.setOnClickListener{
            if (mediaPlayer.isPlaying){
                binding.imgControl.setImageDrawable(resources.getDrawable(R.drawable.play_green))
                mediaPlayer.pause()
            }else{
                binding.imgControl.setImageDrawable(resources.getDrawable(R.drawable.pause))
                mediaPlayer.start()
                updateSeekBar()
            }
        }
    }

    private fun updateSeekBar() {
        lifecycleScope.launch(Dispatchers.Main) {
            while (mediaPlayer.isPlaying) {
                binding.wave.progress = mediaPlayer.currentPosition.toFloat()
                kotlinx.coroutines.delay(500) // فاصله زمانی برای به‌روزرسانی
            }
        }
    }
    private fun processAudio() {
        val fakeWaveform = IntArray(100) { (0..100).random() } // نمونه داده جعلی
        binding.wave.sample = fakeWaveform
    }
    private fun downloadAudioFile(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val inputStream = response.body?.byteStream()
                    val file = File(context?.cacheDir, "temp_audio_file.mp3")
                    val outputStream = FileOutputStream(file)
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream.close()

                    withContext(Dispatchers.Main) {
                        setupMediaPlayer(file)
                        processAudio()
                    }
                }
            } catch (e: Exception) {
                Log.e("DownloadError", e.message.toString())
            }
        }
    }

    private fun setupMediaPlayer(file: File) {
        mediaPlayer.setDataSource(file.path)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            binding.wave.maxProgress = it.duration.toFloat()
            it.start()
            updateSeekBar()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}
