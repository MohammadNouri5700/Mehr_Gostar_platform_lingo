package com.android.platform.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.ActivityCourseBinding
import com.android.platform.databinding.ActivityMainBinding
import com.android.platform.ui.course.CourseViewModel
import com.android.platform.ui.home.HomeFragment
import com.android.platform.ui.learn.LearnFragment
import com.android.platform.ui.report.ReportFragment
import com.android.platform.utils.extension.showToast
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mainViewModel: MainViewModel by viewModels { viewModelFactory }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as PlatformApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mainViewModel
        setContentView(binding.root)



        mainViewModel.event.observe(this, Observer {
            when (it) {
                "Home" -> {
                    showFragment(HomeFragment())
                }

                "Learn" -> {
                    showFragment(LearnFragment())
                }

                "Report" -> {
                    showFragment(ReportFragment())
                }



                "Profile" -> {

                }
            }
        })
        initMain()

    }

    private fun changeTheme() {
        val isLightTheme = true
        val newTheme = if (isLightTheme) R.style.Theme_App_Dark else R.style.Theme_App_Light

        val contextThemeWrapper = ContextThemeWrapper(this, newTheme)
        val inflater = layoutInflater.cloneInContext(contextThemeWrapper)

        setContentView(inflater.inflate(R.layout.activity_main, null))
    }

    private fun showFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment?.javaClass != fragment.javaClass) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    private var doubleBackToExitPressedOnce = false

    private fun initMain() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finishAffinity()
                } else {
                    doubleBackToExitPressedOnce = true
                    showToast(R.string.doublebackmessage)
                    mainViewModel.viewModelScope.launch {
                        delay(2000)
                        doubleBackToExitPressedOnce = false
                    }
                }
            }
        })
    }

}