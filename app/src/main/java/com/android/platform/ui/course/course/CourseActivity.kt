package com.android.platform.ui.course.course

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
import com.android.platform.di.factory.LoadingDialog
import com.android.platform.ui.course.course.adapter.CourseAdapter
import com.android.platform.ui.course.course.ui.SelectCourseItemDialog
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class CourseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var loadingDialog: LoadingDialog


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var courseAdapter: CourseAdapter

    private val viewModel: CourseActivityViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivityCourseBinding

    private var lessonId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );


        (applicationContext as PlatformApplication).appComponent.inject(this)


        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course)
        binding.viewModel = viewModel
        setContentView(binding.root)

        binding.recList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        lessonId = intent.getIntExtra("LESSON_ID", -1)
        binding.lblTitle.text = intent.getStringExtra("LESSON_NAME")
        binding.lblDuration.text = intent.getStringExtra("LESSON_DURATION")

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
                            lessonReply.exercisesCount.let {
                                binding.lblPractice.text = it.toString()
                            }
                        }
                        binding.recList.adapter = courseAdapter
                    }
                    viewModel.call.enqueueMainTask {
                        loadingDialog.dismiss()
                    }
                }

                "Error" -> finish()
            }
        })

        viewModel.selectedLessonId.observe(this) { lessonId ->
            lessonId?.let {
                viewModel.lessonReply?.let { lessonReplay ->
                    viewModel.call.enqueueMainTask {
                        val bottomSheetFragment = SelectCourseItemDialog(lessonReplay,lessonId,viewModel,this)
                        bottomSheetFragment.show(supportFragmentManager, "SelectCourseItemDialog")
                    }
                }
            }



//                val intent = Intent(activity, CourseList::class.java)
//                intent.putExtra("LEVEL_ID", it)
//                intent.putExtra("LEVEL_NAME", viewModel.levelsReply?.levelsList?.find { item-> item.levelId==it }?.title)
//                startActivity(intent)
//                activity?.overridePendingTransition(0, android.R.anim.fade_out);

        }

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

        loadingDialog.changeContext(this)
        loadingDialog.show()

    }

    private fun initCoursePlayer(value: String) {
        binding.customExoPlayerView.setSource(value)
    }

}