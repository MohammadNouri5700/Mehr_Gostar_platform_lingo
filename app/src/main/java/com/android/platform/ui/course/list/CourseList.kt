package com.android.platform.ui.course.list

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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
import com.android.platform.di.factory.LoadingView
import com.android.platform.di.module.GrpcModule.SiteURL
import com.android.platform.ui.course.course.CourseActivity
import com.android.platform.ui.course.list.adapter.CourseListAdapter
import com.android.platform.utils.extension.hideLoading
import com.android.platform.utils.extension.initFullScreen
import com.android.platform.utils.extension.showLoading
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class CourseList : DaggerAppCompatActivity() {

    @Inject
    @SiteURL
    lateinit var siteUrl: String

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CourseListViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivityCourseListBinding

    private var levelId: Int = -1

    private lateinit var courseListAdapter: CourseListAdapter

    private lateinit var loadingView: LoadingView

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            initFullScreen() // اطمینان از بازگرداندن حالت Full-Screen
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as PlatformApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_list)
        binding.viewModel = viewModel
        initFullScreen()

        setContentView(binding.root)
        loadingView = showLoading()
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
                                initCoursePlayer("${siteUrl}$it")
                        }

                        binding.recList.adapter = courseListAdapter
                    }
                    viewModel.call.enqueueMainTask {
                        hideLoading()
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
                intent.putExtra("LESSON_PRACTICES",  viewModel.lessonsReply?.lessonsList?.find { item-> item.lessonId==it }?.exerciseCount)
                startActivity(intent)
                overridePendingTransition(0, android.R.anim.fade_out);
            }
        }



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