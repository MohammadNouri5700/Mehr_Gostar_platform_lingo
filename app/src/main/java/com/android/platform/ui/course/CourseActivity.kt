package com.android.platform.ui.course

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.ViewModelProvider
import com.android.platform.PlatformApplication
import com.android.platform.data.local.entity.Course
import com.android.platform.databinding.ActivityCourseBinding
import com.android.platform.utils.UiState
import dagger.android.support.DaggerAppCompatActivity

class CourseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val courseViewModel: CourseViewModel by viewModels { viewModelFactory }
    private lateinit var binding: ActivityCourseBinding

    var cnt = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as PlatformApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycleScope.launch {
            courseViewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // Show loading indicator
                    }
                    is UiState.Success -> {
                        cnt++;
                        Toast.makeText(applicationContext,"WE COULD  " + cnt,Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Error -> {
                        // Show error message
                    }
                }
            }
        }

        binding.addButton.setOnClickListener {
            val course = Course(courseName = "New Course", description = "Description")
            courseViewModel.insert(course)
        }
    }
}