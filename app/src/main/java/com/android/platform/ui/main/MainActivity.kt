package com.android.platform.ui.main

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.ActivityMainBinding
import com.android.platform.ui.home.HomeFragment
import com.android.platform.ui.level.LevelFragment
import com.android.platform.ui.report.ReportFragment
import com.android.platform.utils.extension.setPage
import com.android.platform.utils.extension.showToast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.messaging
import com.scottyab.rootbeer.RootBeer
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

    private lateinit var bottomNavImages: List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        val rootBeer = RootBeer(this)
        if (rootBeer.isRooted) {
            showToast("ROOT Device is denied")
            finishAffinity()
        }

        (applicationContext as PlatformApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mainViewModel
        setContentView(binding.root)
        bottomNavImages =
            listOf(binding.imgHome, binding.imgLearn, binding.imgReport, binding.imgProfile)



        mainViewModel.event.observe(this, Observer {
            when (it) {
                "Loading" -> {
                    val packageInfo = this.packageManager.getPackageInfo(packageName, 0)
                    val versionName = packageInfo.versionName
                    binding.txtVersion.text = "  ورژن شماره  : $versionName"

                    loadHeavyFragmentsInBackground()

                }

                "Home" -> {
                    mainViewModel.viewModelScope.launch {
                        val res = withContext(Dispatchers.Main) {
                            showFragment("HOME")
                        }
                        withContext(Dispatchers.Main) {
                            updateConstraintsForView(binding.constraintLayout, binding.imgHome.id)
                            resetAllColor()
                        }
                    }
                }

                "Learn" -> {
                    mainViewModel.viewModelScope.launch {
                        val res = withContext(Dispatchers.Main) {
                            showFragment("LEARN")
                        }
                        withContext(Dispatchers.Main) {
                            updateConstraintsForView(binding.constraintLayout, binding.imgLearn.id)
                            resetAllColor()
                        }

                    }
                }

                "Report" -> {
                    mainViewModel.viewModelScope.launch {
                        val res = withContext(Dispatchers.Main) {
                            showFragment("REPORT")
                        }
                        withContext(Dispatchers.Main) {
                            updateConstraintsForView(binding.constraintLayout, binding.imgReport.id)
                            resetAllColor()
                        }

                    }
                }

                "Profile" -> {
                    resetAllColor()
                    updateConstraintsForView(binding.constraintLayout, binding.imgProfile.id)
                    showFragment("PROFILE")
                }
            }
        })
        initMain()


        val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.logEvent("HomePage", null)

    }


    private fun updateConstraintsForView(motionLayout: MotionLayout, imageViewId: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(motionLayout)

        constraintSet.connect(R.id.view, ConstraintSet.START, imageViewId, ConstraintSet.START)
        constraintSet.connect(R.id.view, ConstraintSet.END, imageViewId, ConstraintSet.END)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator()
        transition.duration = 600

        TransitionManager.beginDelayedTransition(motionLayout, transition)
        constraintSet.applyTo(motionLayout)
        animateToSelect(findViewById(imageViewId))
    }

    private fun animateToSelect(imageView: ImageView) {
        val colorFrom = ContextCompat.getColor(this, R.color.unselectedNavigation)
        val colorTo = ContextCompat.getColor(this, R.color.selectedNavigation)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 300
        colorAnimation.addUpdateListener { animator ->
            imageView.setColorFilter(animator.animatedValue as Int)
        }
        colorAnimation.repeatCount = 0
        colorAnimation.repeatMode = ValueAnimator.REVERSE
        colorAnimation.start()
    }

    private fun animateToDESelect(imageView: ImageView) {

        val colorFrom = ContextCompat.getColor(this, R.color.selectedNavigation)
        val colorTo = ContextCompat.getColor(this, R.color.black)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 0
        colorAnimation.addUpdateListener { animator ->
            imageView.setColorFilter(animator.animatedValue as Int)
        }
        colorAnimation.repeatCount = 0
        colorAnimation.repeatMode = ValueAnimator.REVERSE
        colorAnimation.start()
    }

    private fun resetAllColor() {
        bottomNavImages.forEach { imageView ->
            animateToDESelect(imageView)
        }
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

    private fun showFragment(tag: String) {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.setCustomAnimations(
            R.anim.fade_in,
            R.anim.fade_out
        )

        supportFragmentManager.fragments.forEach { fragment ->
            transaction.hide(fragment)
        }

        supportFragmentManager.findFragmentByTag(tag)?.let { fragment ->
            transaction.show(fragment)
            transaction.commitAllowingStateLoss()
            mainViewModel.viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    setPage(tag, fragment::class.java.simpleName)
                }
            }
        } ?: run {
            showToast("Fragment with tag $tag not found")
        }


    }


    private var doubleBackToExitPressedOnce = false

    private fun initMain() {
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

        mainViewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Firebase.messaging.token.addOnCompleteListener(
                    OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                            return@OnCompleteListener
                        }

                        // Get new FCM registration token
                        val token = task.result


                        Log.d("APP", "TOKEN IS == $token")

                    })
            }
        }
    }

    private fun loadHeavyFragmentsInBackground() {
        mainViewModel.viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                val fragments = listOf(
                    HomeFragment() to "HOME",
                    LevelFragment() to "LEARN",
                    ReportFragment() to "REPORT",
                    ReportFragment() to "PROFILE"
                )

                withContext(Dispatchers.Main) {
                    val transaction = supportFragmentManager.beginTransaction()

                    fragments.forEach { (fragment, tag) ->
                        transaction.add(R.id.fragment_container, fragment, tag).hide(fragment)
                    }

                    transaction.commitNowAllowingStateLoss()
                }
            }
            delay(10)
            loadUI()
        }
    }


    private fun loadUI() {
        val animator =
            ObjectAnimator.ofFloat(binding.bottomNav, "translationY", 200f, 0f)
        animator.setDuration(0)//TODO TIME
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
        ObjectAnimator.ofFloat(binding.conLoading, "alpha", 1f, 0f).apply {
            duration = 0  // مدت زمان انیمیشن
        }.start()
        mainViewModel.openReport()
    }
}