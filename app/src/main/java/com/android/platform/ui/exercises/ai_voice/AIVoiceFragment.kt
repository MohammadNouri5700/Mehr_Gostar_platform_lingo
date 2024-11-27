package com.android.platform.ui.exercises.ai_voice


import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.platform.ExerciseModel
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentAiVoiceExerciseBinding
import com.android.platform.databinding.FragmentContextPlacementExerciseBinding
import com.android.platform.databinding.FragmentDetectExerciseBinding
import com.android.platform.databinding.FragmentGeneralExerciseBinding
import com.android.platform.repository.data.model.BotMessageEntity
import com.android.platform.repository.data.model.MType
import com.android.platform.ui.global.AiBotAdapter
import com.android.platform.utils.extension.showToast
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import okhttp3.internal.notify
import javax.inject.Inject


class AIVoiceFragment @Inject constructor(val value: ExerciseModel) : Fragment() {



    private var service: AudioRecordService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as AudioRecordService.LocalBinder
            this@AIVoiceFragment.service = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(activity, AudioRecordService::class.java).also { intent ->
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            activity?.unbindService(connection)
            isBound = false
        }
    }




    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AIVoiceViewModel

    private lateinit var binding: FragmentAiVoiceExerciseBinding

    lateinit var itemsAdapter: AiBotAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_ai_voice_exercise, container, false)
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[AIVoiceViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        itemsAdapter = AiBotAdapter(viewModel.messageList)


        binding.recMessages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.recMessages.adapter=itemsAdapter

        binding.imgSend.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {


//                    Handler(Looper.getMainLooper()).postDelayed({
//                        audioRecorderThread = AudioRecorderThread(requireContext(), "recorded_audio.pcm")
//                        audioRecorderThread.start()
//                    }, 500)

                    viewModel.startListening(requireActivity())
                    binding.imgSend.alpha = 0.5f
                }

                MotionEvent.ACTION_UP -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewModel.stopListening()
//                        service?.stopRecording()
//                        audioRecorderThread.stopRecording()
                    }, 1000)
                    binding.imgSend.alpha = 1.0f
                }

                MotionEvent.ACTION_CANCEL -> {

                }
            }
            true
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recMessages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        viewModel.event.observe(viewLifecycleOwner, Observer { data ->
            viewModel.messageList.add(BotMessageEntity(MType.messageText,data,"",""))
            binding.recMessages.adapter?.notifyItemChanged(viewModel.messageList.size)
        })


    }


}