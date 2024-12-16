package com.android.platform.ui.exercises.listening

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.platform.ExerciseModel
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentListeningExerciseBinding
import com.android.platform.ui.exercises.ExerciseViewModel
import com.android.platform.ui.exercises.listening.adapter.AdapterListening
import com.android.platform.ui.exercises.order.adapter.OrderListAdapter
import com.masoudss.lib.SeekBarOnProgressChanged
import com.masoudss.lib.WaveformSeekBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class ListeningFragment @Inject constructor(val value: ExerciseModel) : Fragment() {
    private lateinit var sharedViewModel: ExerciseViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ListeningViewModel
    private lateinit var binding: FragmentListeningExerciseBinding
    private lateinit var mediaPlayer: MediaPlayer
    lateinit var audioFile: File
    lateinit var adapter: AdapterListening


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
        sharedViewModel = ViewModelProvider(this, viewModelFactory)[ExerciseViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.value=value
        binding.lifecycleOwner = viewLifecycleOwner
        mediaPlayer = MediaPlayer()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.initList()

        binding.recListening.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.call.enqueueIoTask {
            downloadAudioFile("https://dl.lingomars.ir/general/sway.mp3")
        }

        binding.wave.onProgressChanged = object : SeekBarOnProgressChanged {
            override fun onProgressChanged(
                waveformSeekBar: WaveformSeekBar,
                progress: Float,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress.toInt())
                }

            }
        }
        viewModel.event.observe(viewLifecycleOwner, Observer { data ->
            when (data) {
                "Init" -> {
                    adapter = AdapterListening(viewModel.contentList,viewModel,requireContext())
                    binding.recListening.adapter = adapter
                }

                "Update" -> {
                    adapter = AdapterListening(viewModel.contentList,viewModel,requireContext())
                    binding.recListening.adapter = adapter
                }

                "FinishQuestion"->{
                    binding.btnConfirm.visibility = View.VISIBLE
                }
                "Confirm" -> {
                    sharedViewModel.confirmExercise()
                }

                "FinishAudio" -> {
                    resetPlayer()
                }
            }
        })

        binding.imgControl.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                pausePlayer()
            } else {
                playPlayer()
            }
        }
    }

    private fun resetPlayer() {
       pausePlayer()
        mediaPlayer.seekTo(0)
        binding.wave.progress=0f
    }

    private fun pausePlayer() {
        binding.imgControl.setImageDrawable(resources.getDrawable(R.drawable.play_green))
        mediaPlayer.pause()
    }

    private fun playPlayer() {
        binding.imgControl.setImageDrawable(resources.getDrawable(R.drawable.pause))
        mediaPlayer.start()
        updateSeekBar()
    }

    private fun updateSeekBar() {
        lifecycleScope.launch(Dispatchers.Main) {
            while (mediaPlayer.isPlaying) {
                binding.wave.progress = mediaPlayer.currentPosition.toFloat()
                kotlinx.coroutines.delay(10)
            }
            if (990.0f <= (binding.wave.progress * 1000 / binding.wave.maxProgress)) {
                viewModel.finishAudio()
            }
        }
    }

    private fun processAudio() {
        val fakeWaveform = IntArray(100) { (0..100).random() }
        binding.wave.sample = fakeWaveform
    }

    private fun downloadAudioFile(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        viewModel.call.enqueueIoTask {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val fileName = UUID.randomUUID().toString() + ".mp3"
                    val inputStream = response.body?.byteStream()
                    audioFile = File(context?.cacheDir, fileName)
                    val outputStream = FileOutputStream(audioFile)
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream.close()

                    withContext(Dispatchers.Main) {
                        setupMediaPlayer(audioFile)
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
