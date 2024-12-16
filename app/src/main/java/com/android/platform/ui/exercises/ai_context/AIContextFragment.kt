package com.android.platform.ui.exercises.ai_context


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.platform.ExerciseModel
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentAiContextExerciseBinding
import com.android.platform.ui.global.BotChatViewModel
import com.android.platform.ui.exercises.ExerciseViewModel
import com.android.platform.ui.global.AiBotAdapter
import javax.inject.Inject


class AIContextFragment @Inject constructor(val value: ExerciseModel) : Fragment() {

    private lateinit var sharedViewModel: ExerciseViewModel
    private lateinit var botChatViewModel: BotChatViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AIContextViewModel

    private lateinit var binding: FragmentAiContextExerciseBinding

    lateinit var itemsAdapter: AiBotAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_ai_context_exercise,
            container,
            false
        )
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[AIContextViewModel::class.java]
        sharedViewModel = ViewModelProvider(this, viewModelFactory)[ExerciseViewModel::class.java]
        botChatViewModel = ViewModelProvider(this, viewModelFactory)[BotChatViewModel::class.java]
        binding.viewModel = viewModel
        binding.chatViewModel = botChatViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        botChatViewModel.value = value
        itemsAdapter = AiBotAdapter(botChatViewModel.messageList,requireContext(),viewModel.call)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        botChatViewModel.initList()
        binding.recMessages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        botChatViewModel.messageUser.observe(viewLifecycleOwner, Observer { data ->
            viewModel.call.enqueueMainTask {
                binding.edtMessage.setText(data)
            }
        })

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