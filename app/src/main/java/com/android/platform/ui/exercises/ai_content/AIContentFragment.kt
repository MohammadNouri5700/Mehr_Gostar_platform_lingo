package com.android.platform.ui.exercises.ai_content


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.platform.ExerciseModel
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentAiContentExerciseBinding
import com.android.platform.databinding.FragmentAiContextExerciseBinding
import com.android.platform.databinding.FragmentAiLatterExerciseBinding
import com.android.platform.databinding.FragmentAiVoiceExerciseBinding
import com.android.platform.databinding.FragmentContextPlacementExerciseBinding
import com.android.platform.databinding.FragmentDetectExerciseBinding
import com.android.platform.databinding.FragmentGeneralExerciseBinding
import com.android.platform.ui.exercises.ExerciseViewModel
import javax.inject.Inject


class AIContentFragment @Inject constructor(val value: ExerciseModel) : Fragment() {

    private lateinit var sharedViewModel: ExerciseViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AIContentViewModel

    private lateinit var binding: FragmentAiContentExerciseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_ai_content_exercise,
            container,
            false
        )
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[AIContentViewModel::class.java]
        sharedViewModel = ViewModelProvider(this, viewModelFactory)[ExerciseViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.value = value
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lblReading.text = viewModel.value.content


        viewModel.event.observe(viewLifecycleOwner, Observer { data ->
            when (data) {
                "StartVoice" -> {
                    viewModel.call.enqueueMainTask {
                        binding.imgRecord.apply {
                            alpha = 0f;
                            setImageResource(R.drawable.voice_pause)
                        }.animate().alpha(1f).setDuration(700).start()

                        binding.lblTimer.apply {
                            alpha = 0f
                        }.animate().alpha(1f).setDuration(700).start()

                    }
                }

                "StopVoice" -> {
                    viewModel.call.enqueueMainTask {
                        binding.imgRecord.apply {
                            alpha = 0f;
                            setImageResource(R.drawable.voice)
                        }.animate().alpha(1f).setDuration(700).start()

                        binding.lblTimer.apply {
                            alpha = 1f
                        }.animate().alpha(0f).setDuration(700).start()

                    }
                }

            }
        })


    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopTimer()
    }


}