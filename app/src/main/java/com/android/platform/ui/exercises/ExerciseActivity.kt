package com.android.platform.ui.exercises


import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.platform.ExerciseModel
import com.android.platform.LessonReply
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.ActivityExerciseBinding
import com.android.platform.ui.exercises.ai_content.AIContentFragment
import com.android.platform.ui.exercises.ai_context.AIContextFragment
import com.android.platform.ui.exercises.ai_letter.AILetterFragment
import com.android.platform.ui.exercises.ai_voice.AIVoiceFragment
import com.android.platform.ui.exercises.context_placement.ContextPlacementFragment
import com.android.platform.ui.exercises.listening.ListeningFragment
import com.android.platform.ui.exercises.order.OrderFragment
import com.android.platform.ui.exercises.placement.PlacementFragment
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class ExerciseActivity : DaggerAppCompatActivity() {



    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ExerciseViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivityExerciseBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        (applicationContext as PlatformApplication).appComponent.inject(this)


        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise)
        binding.viewModel = viewModel
        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            val decorView = window.decorView
            val uiOptions = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            decorView.systemUiVisibility = uiOptions
        }


        viewModel.lessonReply = LessonReply.parseFrom(intent.getByteArrayExtra("ITEM"));
        viewModel.exerciseId = intent.getIntExtra("EXERCISE_ID", -1)

        val exercise: ExerciseModel = viewModel.lessonReply.contentsList
            .flatMap { it.exercisesList }
            .find { it.id == viewModel.exerciseId }!!

        binding.lblTitle.text = exercise.exerciseType.name


        when (exercise.exerciseType.name) {
            "Order" -> {
                showFragment(OrderFragment(exercise))
            }
            "Placement"->{
                showFragment(PlacementFragment(exercise))
            }
            "ListeningExercise"->{
                showFragment(ListeningFragment(exercise))
            }
            "AiContext"->{
                showFragment(AIContextFragment(exercise))
            }
            "AiVoice"->{
                showFragment(AIVoiceFragment(exercise))
            }
            "AiContent"->{
                showFragment(AIContentFragment(exercise))
            }
            "AiLetter"->{
                showFragment(AILetterFragment(exercise))
            }
            "ContextPlacement"->{
                showFragment(ContextPlacementFragment(exercise))
            }
        }




        viewModel.event.observe(this, Observer { data ->
            when (data) {
                "ConfirmExercise" -> {
                    finish()
                }
                "Close"->{finish()}
            }
        })

    }

    private fun showFragment(value: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, value)
            .commit()
    }

    override fun onResume() {
        super.onResume()
    }


}