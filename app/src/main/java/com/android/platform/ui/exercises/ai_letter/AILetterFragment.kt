package com.android.platform.ui.exercises.ai_letter


import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.platform.ExerciseModel
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentAiLatterExerciseBinding
import com.android.platform.ui.exercises.ExerciseViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject


class AILetterFragment @Inject constructor(val value: ExerciseModel) : Fragment() {

    private lateinit var sharedViewModel: ExerciseViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AILetterViewModel

    private lateinit var binding: FragmentAiLatterExerciseBinding

    private var statusRepeat = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_ai_latter_exercise,
            container,
            false
        )
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[AILetterViewModel::class.java]
        sharedViewModel = ViewModelProvider(this, viewModelFactory)[ExerciseViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.value = value


        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lblQuestion.text = viewModel.value.content



        viewModel.event.observe(viewLifecycleOwner, Observer { data ->
            when (data) {
                "Confirm" -> {
                    sharedViewModel.confirmExercise()
                }
            }
        })


        viewModel.essay.observe(viewLifecycleOwner, Observer { newText ->
            viewModel.setCount()
        })

        binding.root.getViewTreeObserver()
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    var r = Rect()
                    view.getWindowVisibleDisplayFrame(r)
                    var heightDiff = view.getRootView().height - (r.bottom - r.top);

                    if (heightDiff > 500) {
                        if (!statusRepeat) {
                            statusRepeat = true
                            viewModel.call.enqueueMainTask {
                                binding.conTest.visibility = View.GONE
                            }
                        }
                    } else {
                        if (statusRepeat) {
                            statusRepeat = false

                            viewModel.call.enqueueMainTask {
                                binding.conTest.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            })

    }


}