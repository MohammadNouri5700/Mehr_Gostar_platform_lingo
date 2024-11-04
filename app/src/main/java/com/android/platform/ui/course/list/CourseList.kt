package com.android.platform.ui.course.list

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.ActivityCourseListBinding
import com.android.platform.databinding.ActivityMainBinding
import com.android.platform.di.factory.FullscreenVideoDialog
import com.android.platform.ui.course.course.CourseActivity
import com.android.platform.ui.course.list.adapter.CourseListAdapter
import com.android.platform.ui.level.levels.LevelAdapter
import com.android.platform.ui.main.MainViewModel
import com.android.platform.ui.registeration.Login
import com.android.platform.utils.extension.showToast
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class CourseList : DaggerAppCompatActivity() {
    private lateinit var player: ExoPlayer
    private var isFullScreen = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mainViewModel: CourseListViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivityCourseListBinding

    private var levelId : Int = -1

    private lateinit var courseListAdapter: CourseListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );


        (applicationContext as PlatformApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_list)
        binding.viewModel = mainViewModel
        setContentView(binding.root)
//
        levelId  = intent.getIntExtra("LEVEL_ID", -1)
        binding.lblTitle.text = intent.getStringExtra("LEVEL_NAME")

        mainViewModel.loadList(levelId)

        binding.recList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

//        val fullscreenDialog = FullscreenVideoDialog()
//        fullscreenDialog.setPlayer(player)
//        fullscreenDialog.show(supportFragmentManager, "FullscreenVideoDialogFragment")

        mainViewModel.event.observe(this, Observer {
            when (it) {
                "Loading" -> {
                    initCoursePlayer()
                }
                "Back"->{
                    onBackPressed()
                }
                "LoadList"->{
                    mainViewModel.call.enqueueMainTask {
                        courseListAdapter =
                            mainViewModel.lessonsReply?.let {
                                CourseListAdapter(
                                    it,
                                    mainViewModel
                                )
                            }!!
                        binding.recList.adapter = courseListAdapter
                    }
                }
            }
        })

        mainViewModel.selectedLessonId.observe(this) { lessonId ->
            lessonId?.let {
//                showToast("lessonId == $lessonId")
                val intent = Intent(this, CourseActivity::class.java)
                intent.putExtra("LESSON_ID", it)
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

    }


    private fun initCoursePlayer(){
        player = ExoPlayer.Builder(this).build()
        binding.customExoPlayerView.setPlayer(player)
        val mediaItem = MediaItem.fromUri("https://dl.lingomars.ir/general/video.mp4")
        player.setMediaItem(mediaItem)
//        player.prepare()
//        player.playWhenReady = true
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