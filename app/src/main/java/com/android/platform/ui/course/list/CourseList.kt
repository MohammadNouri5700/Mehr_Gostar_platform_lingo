package com.android.platform.ui.course.list

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
import com.android.platform.databinding.ActivityCourseListBinding
import com.android.platform.di.factory.LoadingDialog
import com.android.platform.ui.course.course.CourseActivity
import com.android.platform.ui.course.list.adapter.CourseListAdapter
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class CourseList : DaggerAppCompatActivity() {

    @Inject
    lateinit var loadingDialog: LoadingDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CourseListViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivityCourseListBinding

    private var levelId: Int = -1

    private lateinit var courseListAdapter: CourseListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );


        (applicationContext as PlatformApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_list)
        binding.viewModel = viewModel
        setContentView(binding.root)
//
        levelId = intent.getIntExtra("LEVEL_ID", -1)
        binding.lblTitle.text = intent.getStringExtra("LEVEL_NAME")

        viewModel.loadList(levelId)

        binding.recList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)



        viewModel.event.observe(this, Observer { it ->
            when (it) {
                "Loading" -> {

                }

                "Back" -> {
                    onBackPressed()
                }
                "Error"-> finish()

                "LoadList" -> {
                    viewModel.call.enqueueMainTask {
                        courseListAdapter =
                            viewModel.lessonsReply?.let {
                                CourseListAdapter(
                                    it,
                                    viewModel
                                )
                            }!!
                        viewModel.lessonsReply?.levelVideo?.let {
                            if (it == "")
                                binding.customExoPlayerView.visibility = View.GONE
                            else
                                initCoursePlayer(it)
                        }

                        binding.recList.adapter = courseListAdapter
                    }
                    viewModel.call.enqueueMainTask {
                        loadingDialog.dismiss()
                    }
                }
            }
        })

        viewModel.selectedLessonId.observe(this) { lessonId ->
            lessonId?.let {
                val intent = Intent(this, CourseActivity::class.java)
                intent.putExtra("LESSON_ID", it)
                intent.putExtra("LESSON_NAME",  viewModel.lessonsReply?.lessonsList?.find { item-> item.lessonId==it }?.title)
                intent.putExtra("LESSON_DURATION",  viewModel.lessonsReply?.lessonsList?.find { item-> item.lessonId==it }?.duration)
                startActivity(intent)
            }
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


    private fun initCoursePlayer(value:String) {
        binding.customExoPlayerView.setSource(value)
    }


    override fun onBackPressed() {
        finish()
//        overridePendingTransition(0, android.R.anim.fade_out);
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()

    }
}