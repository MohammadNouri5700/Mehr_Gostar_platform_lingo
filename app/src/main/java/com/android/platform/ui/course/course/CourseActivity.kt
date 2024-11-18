package com.android.platform.ui.course.course

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.ActivityCourseBinding
import com.android.platform.di.factory.LoadingView
import com.android.platform.ui.course.course.adapter.CourseAdapter
import com.android.platform.ui.course.course.ui.SelectCourseItemDialog
import com.android.platform.ui.exercises.ExerciseActivity
import com.android.platform.utils.extension.hideLoading
import com.android.platform.utils.extension.initFullScreen
import com.android.platform.utils.extension.showLoading
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class CourseActivity : DaggerAppCompatActivity() {

    private lateinit var loadingView: LoadingView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var courseAdapter: CourseAdapter

    private val viewModel: CourseActivityViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivityCourseBinding

    private var lessonId: Int = -1

    override fun onStart() {
        super.onStart()
        initFullScreen()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as PlatformApplication).appComponent.inject(this)


        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course)
        binding.viewModel = viewModel

        setContentView(binding.root)
        loadingView = showLoading()
        binding.recList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        lessonId = intent.getIntExtra("LESSON_ID", -1)
        binding.lblTitle.text = intent.getStringExtra("LESSON_NAME")
        binding.lblDuration.text = intent.getStringExtra("LESSON_DURATION")
        binding.lblPractice.text = intent.getIntExtra("LESSON_PRACTICES",0).toString()

        viewModel.loadLesson(lessonId)

        viewModel.event.observe(this, Observer { it ->
            when (it) {
                "Loading" -> {

                }

                "Back" -> {
                    onBackPressed()
                }

                "LoadLesson" -> {
                    viewModel.call.enqueueMainTask {
                        viewModel.lessonReply?.let { lessonReply ->
                            courseAdapter = CourseAdapter(lessonReply, viewModel, this)
                            lessonReply.video?.let {
                                if (it == "")
                                    binding.customExoPlayerView.visibility = View.GONE
                                else
                                    initCoursePlayer(it)
                            }
                        }
                        binding.recList.adapter = courseAdapter
                    }
                    viewModel.call.enqueueMainTask {
                        hideLoading()
                    }
                }

                "Error" -> finish()
            }
        })

        viewModel.selectedLessonId.observe(this) { itemId ->
            itemId?.let {
                viewModel.lessonReply?.let { lessonReplay ->
                    viewModel.call.enqueueMainTask {
                        val bottomSheetFragment = SelectCourseItemDialog(lessonReplay,itemId,viewModel,this)
                        bottomSheetFragment.show(supportFragmentManager, "SelectCourseItemDialog")
                    }
                }
            }
        }
        viewModel.selectedExerciseId.observe(this) { itemId ->
            itemId?.let {
                val intent = Intent(this, ExerciseActivity::class.java)
                intent.putExtra("EXERCISE_ID", it)
                intent.putExtra("ITEM",  viewModel.lessonReply?.toByteArray())
                startActivity(intent)
            }
        }







    }

    private fun initCoursePlayer(value: String) {
        binding.customExoPlayerView.setSource(value)
    }

}