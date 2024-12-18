package com.android.platform.ui.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
//import com.android.platform.LoginReply
//import com.android.platform.LoginRequest
import com.android.platform.PlatformApplication
import com.android.platform.R
//import com.android.platform.UserGrpcServiceGrpc
import com.android.platform.databinding.ActivityMainBinding
import com.android.platform.ui.home.HomeFragment
import com.android.platform.ui.level.LevelFragment
import com.android.platform.ui.profile.ProfileFragment
import com.android.platform.ui.registeration.Login
import com.android.platform.ui.report.ReportFragment
import com.android.platform.utils.extension.initFullScreen
import com.android.platform.utils.extension.setPage
import com.android.platform.utils.extension.showToast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.messaging
import com.scottyab.rootbeer.RootBeer
import dagger.android.support.DaggerAppCompatActivity
//import io.grpc.Context

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.engine.TextToSpeechEngine
import java.util.Locale
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mainViewModel: MainViewModel by viewModels { viewModelFactory }
    private lateinit var binding: ActivityMainBinding

    private lateinit var bottomNavImages: List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );

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
                    binding.txtVersion.text = "${resources.getText(R.string.version)} : $versionName"
                    mainViewModel.viewGetToken(this)
                }

                "ErrorLogin" -> {
                    showToast("Authentication Error")
                    mainViewModel.preferences.putString("PHONE", "")
                    mainViewModel.preferences.putString("TOKEN", "")
                    startActivity(Intent(this, Login::class.java))
                    overridePendingTransition(0, android.R.anim.fade_out);
                    finish()
                }

                "Init" -> {
                    initMain()
                }

                "Home" -> {
                    mainViewModel.call.enqueueMainTask {
                        showFragment("HOME")
                        resetAllColor()
                        updateConstraintsForView(binding.constraintLayout, binding.imgHome.id)
                    }
                }

                "Learn" -> {
                    mainViewModel.call.enqueueMainTask {
                        showFragment("LEARN")
                        resetAllColor()
                        updateConstraintsForView(binding.constraintLayout, binding.imgLearn.id)
                    }
                }

                "Report" -> {
                    mainViewModel.call.enqueueMainTask {
                        showFragment("REPORT")
                        resetAllColor()
                        updateConstraintsForView(binding.constraintLayout, binding.imgReport.id)
                    }
                }

                "Profile" -> {
                    mainViewModel.call.enqueueMainTask {
                        showFragment("PROFILE")
                        resetAllColor()
                        updateConstraintsForView(binding.constraintLayout, binding.imgProfile.id)
                    }
                }
            }
        })



    }


    override fun onStart() {
        super.onStart()
        initFullScreen()
    }

    override fun onResume() {
        super.onResume()
        setTag()
        val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.logEvent("OpenApp", null)

    }

    private fun setTag() {
        if (binding.imgHome.tag == null)
            binding.imgHome.tag = "INFILL"
        if (binding.imgLearn.tag == null)
            binding.imgLearn.tag = "INFILL"
        if (binding.imgReport.tag == null)
            binding.imgReport.tag = "INFILL"
        if (binding.imgProfile.tag == null)
            binding.imgProfile.tag = "INFILL"
    }

    private fun updateConstraintsForView(motionLayout: MotionLayout, imageViewId: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(motionLayout)

        constraintSet.connect(R.id.view, ConstraintSet.START, imageViewId, ConstraintSet.START)
        constraintSet.connect(R.id.view, ConstraintSet.END, imageViewId, ConstraintSet.END)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator()
        transition.duration = 700

        TransitionManager.beginDelayedTransition(motionLayout, transition)
        constraintSet.applyTo(motionLayout)
        animateToSelect(findViewById(imageViewId))
    }

    private fun animateToSelect(imageView: ImageView) {
        mainViewModel.call.enqueueMainTask {
            when (imageView.id) {
                R.id.imgHome -> {
                    binding.imgHome.apply {
                        alpha = 0f; binding.imgHome.tag =
                        "FILL"; setImageResource(R.drawable.home_fill)
                    }.animate().alpha(1f).setDuration(700).start()
                }

                R.id.imgLearn -> {
                    binding.imgLearn.apply {
                        alpha = 0f; binding.imgLearn.tag =
                        "FILL"; setImageResource(R.drawable.book_fill)
                    }.animate().alpha(1f).setDuration(700).start()
                }

                R.id.imgReport -> {
                    binding.imgReport.apply {
                        alpha = 0f;binding.imgReport.tag =
                        "FILL"; setImageResource(R.drawable.chart_fill)
                    }.animate().alpha(1f).setDuration(700).start()
                }

                R.id.imgProfile -> {
                    binding.imgProfile.apply {
                        alpha = 0f;binding.imgProfile.tag =
                        "FILL"; setImageResource(R.drawable.user_fill)
                    }.animate().alpha(1f).setDuration(700).start()
                }
            }
        }

    }

    private fun animateToDESelect(imageView: ImageView) {
        mainViewModel.call.enqueueMainTask {
            when (imageView.id) {
                R.id.imgHome -> {
                    if (binding.imgHome.tag == "FILL") binding.imgHome.apply {
                        tag = "UNFILL"; alpha = 0.7F;setImageResource(R.drawable.home_light)
                    }
                        .animate().alpha(1f).setDuration(700).start()
                }

                R.id.imgLearn -> {
                    if (binding.imgLearn.tag == "FILL") binding.imgLearn.apply {
                        tag = "UNFILL"; alpha = 0.7F;setImageResource(R.drawable.book_light)
                    }
                        .animate().alpha(1f).setDuration(700).start()
                }

                R.id.imgReport -> {
                    if (binding.imgReport.tag == "FILL") binding.imgReport.apply {
                        alpha = 0.7F;
                        tag = "UNFILL";
                        setImageResource(
                            R.drawable.chart_light
                        )
                    }.animate().alpha(1f).setDuration(700).start()
                }

                R.id.imgProfile -> {
                    if (binding.imgProfile.tag == "FILL") binding.imgProfile.apply {
                        alpha = 0.7F;
                        tag = "UNFILL";
                        setImageResource(
                            R.drawable.user_light
                        )
                    }.animate().alpha(1f).setDuration(700).start()
                }
            }
        }
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
            mainViewModel.call.enqueueIoTask {
                setPage(tag, fragment::class.java.simpleName)
            }
        } ?: run {
            showToast("Fragment with tag $tag not found")
        }


    }


    private var doubleBackToExitPressedOnce = false

    private fun initMain() {


        loadHeavyFragmentsInBackground()

        mainViewModel.call.enqueueIoTask {
            mainViewModel.getTest()
        }




        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finishAffinity()
                } else {
                    doubleBackToExitPressedOnce = true
                    showToast(R.string.doublebackmessage)
                    mainViewModel.call.enqueueMainTask {
                        delay(2000)
                        doubleBackToExitPressedOnce = false
                    }
                }
            }
        })

        mainViewModel.viewModelScope.launch {


            mainViewModel.call.enqueueIoTask {
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

            mainViewModel.call.enqueueApplicationTask {
                val fragments = listOf(
                    HomeFragment() to "HOME",
                    LevelFragment() to "LEARN",
                    ReportFragment() to "REPORT",
                    ProfileFragment() to "PROFILE"
                )

                mainViewModel.call.enqueueMainTask {
                    val transaction = supportFragmentManager.beginTransaction()

                    fragments.forEach { (fragment, tag) ->
                        transaction.add(R.id.fragment_container, fragment, tag).hide(fragment)
                    }

                    transaction.commitNowAllowingStateLoss()
                    delay(100)
                    loadUI()
                }
            }

        }

    }


    private fun loadUI() {
        mainViewModel.call.enqueueMainTask {
            binding.bottomNav.animate().translationY(0f).setDuration(1000).start()
            AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(
                        binding.bottomNav,
                        "translationY",
                        binding.bottomNav.height.toFloat(),
                        0F
                    ),
                    ObjectAnimator.ofFloat(binding.bottomNav, "alpha", 0f, 1f)
                )
                duration = 1000
                start()
            }
        }
        mainViewModel.openFirst()
        mainViewModel.call.enqueueMainTask {
            delay(1000)
            binding.conLoading.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Speech.getInstance().shutdown();
    }
}