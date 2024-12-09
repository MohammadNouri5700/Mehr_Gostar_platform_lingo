package com.android.platform.ui.exercises.ai_latter


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentAiLatterExerciseBinding
import com.android.platform.databinding.FragmentAiVoiceExerciseBinding
import com.android.platform.databinding.FragmentContextPlacementExerciseBinding
import com.android.platform.databinding.FragmentDetectExerciseBinding
import com.android.platform.databinding.FragmentGeneralExerciseBinding
import javax.inject.Inject


class AILatterFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AILatterViewModel

    private lateinit var binding: FragmentAiLatterExerciseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ai_latter_exercise, container, false)
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[AILatterViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root

    }






}