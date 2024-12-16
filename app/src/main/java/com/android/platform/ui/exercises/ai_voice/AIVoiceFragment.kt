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
import android.speech.tts.TextToSpeech
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

import com.android.platform.ui.exercises.ExerciseViewModel
import com.android.platform.ui.global.AiBotAdapter
import com.android.platform.ui.global.BotChatViewModel
import net.gotev.speech.Speech
import java.io.File
import javax.inject.Inject


class AIVoiceFragment @Inject constructor(val value: ExerciseModel) : Fragment() {

    private lateinit var botChatViewModel: BotChatViewModel
    private lateinit var sharedViewModel: ExerciseViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AIVoiceViewModel

    private lateinit var binding: FragmentAiVoiceExerciseBinding

    lateinit var itemsAdapter: AiBotAdapter

    private lateinit var audioRecorder: AudioRecorder

    private lateinit var path: String


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_ai_voice_exercise, container, false)
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[AIVoiceViewModel::class.java]
        botChatViewModel = ViewModelProvider(this, viewModelFactory)[BotChatViewModel::class.java]
        sharedViewModel = ViewModelProvider(this, viewModelFactory)[ExerciseViewModel::class.java]
        binding.viewModel = viewModel
        binding.chatViewModel = botChatViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        botChatViewModel.value = value
        itemsAdapter = AiBotAdapter(botChatViewModel.messageList,requireContext(),viewModel.call)
        audioRecorder = AudioRecorder(requireActivity())


        val on = object : TextToSpeech.OnInitListener{
            override fun onInit(status: Int) {
                Log.e("APP","WE GOT TextToSpeech $status")
            }

        }

        binding.imgSend.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    path = audioRecorder.startRecording()
                    binding.imgSend.alpha = 0.5f
                }

                MotionEvent.ACTION_UP -> {
                    audioRecorder.stopRecording()
//                    viewModel.sendVoice(requireActivity(),File(path))
                    Log.e("APP","WE SEND ${File(path).name} ")
                    botChatViewModel.sendVoice(File(path).name, File(path).path)
                    binding.imgSend.alpha = 1.0f
                }
            }
            true
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        botChatViewModel.initList()
        binding.recMessages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        botChatViewModel.eventBot.observe(viewLifecycleOwner, Observer { data ->
            when (data) {
                "Init" -> {
                    viewModel.call.enqueueMainTask {
                        binding.recMessages.adapter = itemsAdapter
                    }
                }

                "Update" -> {
                    viewModel.call.enqueueMainTask {
                        itemsAdapter.updateList(botChatViewModel.messageList)
                    }
                }

                "Confirm" -> {
                    viewModel.call.enqueueMainTask {
                        sharedViewModel.confirmExercise()
                    }
                }
                "UpdateRemove"->{
                    viewModel.call.enqueueMainTask {
                        itemsAdapter.notifyItemRemoved(botChatViewModel.messageList.size)
                    }
                }

                "ScrollToEnd" -> {
                    viewModel.call.enqueueMainTask {
                        binding.recMessages.scrollToPosition(botChatViewModel.messageList.size - 1)
                    }
                }

                "ClearMessage" -> {
                    viewModel.call.enqueueMainTask {
                        binding.edtMessage.text.clear()
                    }
                }
            }
        })


    }


}