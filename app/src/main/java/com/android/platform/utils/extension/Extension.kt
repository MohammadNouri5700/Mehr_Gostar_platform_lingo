package com.android.platform.utils.extension

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.R
import com.android.platform.di.factory.LoadingView
import com.android.platform.repository.data.database.ImageDao
import com.android.platform.repository.data.model.ImageEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.security.AccessController.getContext
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale



fun Context.calculateCharactersPerLine(textSizeDp: Float): Int {
    // گرفتن عرض صفحه نمایش
    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels

    // تبدیل DP به پیکسل
    val textSizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textSizeDp, displayMetrics)

    // ایجاد Paint برای اندازه‌گیری عرض کاراکترها
    val textPaint = Paint()
    textPaint.textSize = textSizePx

    // محاسبه عرض متوسط هر کاراکتر
    val sampleText = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    val averageCharWidth = textPaint.measureText(sampleText) / sampleText.length

    // محاسبه تعداد کاراکترهایی که در یک خط جا می‌شوند
    return (screenWidth / averageCharWidth).toInt()
}

fun RecyclerView.betterSmoothScrollToPosition(targetItem: Int) {
    layoutManager?.apply {
        val maxScroll = 0
        when (this) {
            is LinearLayoutManager -> {
                val topItem = findFirstVisibleItemPosition()
                val distance = topItem - targetItem
                val anchorItem = when {
                    distance > maxScroll -> targetItem + maxScroll
                    distance < -maxScroll -> targetItem - maxScroll
                    else -> topItem
                }
                if (anchorItem != topItem) scrollToPosition(anchorItem)
                post {
                    smoothScrollToPosition(targetItem)
                }
            }
            else -> smoothScrollToPosition(targetItem)
        }
    }
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.showToast(message: Int) {
    Toast.makeText(this, resources.getText(message), Toast.LENGTH_SHORT).show()
}

fun Activity.setPage(name: String, className: String) {
    val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    val bundle = Bundle().apply {
        putString(FirebaseAnalytics.Param.SCREEN_NAME, name)
        putString(FirebaseAnalytics.Param.SCREEN_CLASS, className)
    }
    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
}
fun RecyclerView.Adapter<*>.animateRight(view: View, duration: Long = 200) {
    view.alpha = 0f
    view.post {
        val startOffsetX = -view.width.toFloat()
        view.translationX = startOffsetX
        view.animate()
            .translationX(0f)
            .alpha(1f)
            .setDuration(duration)
            .start()
    }
}
fun RecyclerView.Adapter<*>.animateLeft(view: View, duration: Long = 200) {
    view.alpha = 0f
    view.post {
        val startOffsetX = view.width.toFloat()
        view.translationX = startOffsetX
        view.animate()
            .translationX(0f)
            .alpha(1f)
            .setDuration(duration)
            .start()
    }
}



fun RecyclerView.Adapter<*>.animateFadeUp(view: View, duration: Long = 500) {
    view.alpha = 0f
    view.post {
        val startOffsetY = view.height.toFloat()/3
        view.translationY = startOffsetY
        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(duration)
            .start()
    }
}
fun RecyclerView.Adapter<*>.animateFadeDown(view: View, duration: Long = 500) {
    view.alpha = 0f
    view.post {
        val startOffsetY = -view.height.toFloat()/3
        view.translationY = startOffsetY
        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(duration)
            .start()
    }
}

fun Activity.initFullScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
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

fun Activity.showLoading(): LoadingView {
    val rootView = findViewById<ViewGroup>(android.R.id.content)

    val existingLoadingView = rootView.findViewWithTag<LoadingView>("LoadingView")
    if (existingLoadingView != null) {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        existingLoadingView.startAnimation(fadeIn)
        return existingLoadingView
    }

    val loadingView = LoadingView(this).apply { tag = "LoadingView" }
    rootView.addView(loadingView)

    val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
    fadeIn.duration=100
    loadingView.startAnimation(fadeIn)
    loadingView.startTimer()
    return loadingView
}
fun Activity.hideLoading() {
    val rootView = findViewById<ViewGroup>(android.R.id.content)
    val existingLoadingView = rootView.findViewWithTag<LoadingView>("LoadingView")
    if (existingLoadingView != null) {
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                rootView.removeView(existingLoadingView)
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        existingLoadingView.handler.postDelayed(Runnable{
            existingLoadingView.startAnimation(fadeOut)
        }, existingLoadingView.hideTimer())
    }
}


suspend fun String.loadImageFromDatabase(imageDao: ImageDao): Bitmap? {
    val imageEntity = imageDao.getImageByUrl(this)
    return imageEntity?.let { byteArrayToBitmap(it.imageData) }
}

suspend fun ViewModel.getFCMToken(): String = withContext(Dispatchers.IO) {
    try {
        FirebaseMessaging.getInstance().token.await()
    } catch (e: Exception) {
        "FCM registration token failed $e"
    }
}
fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}



fun getLastSevenDays(): List<String> {
    return (0..6).map { i ->
        LocalDate.now().minusDays(i.toLong())
            .dayOfWeek
            .getDisplayName(TextStyle.SHORT, Locale("fa","IR"))
    }
}

fun getPercentageOfDay(minutes: Long, totalMinutes: Int = 60): Float {
    if (minutes < 0 || minutes > totalMinutes) {
        return 100.0f
    }
    return (minutes.toFloat() / totalMinutes) * 100
}

fun isValidPhoneNumber(value: String): Boolean {
    val phonePattern = "^0?9\\d{9}$"
    return value.matches(phonePattern.toRegex())
}

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    // Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    // If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
    return byteArray?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
}
fun getAndroidId(context: Context): String {
    return Secure.getString(context.contentResolver, Secure.ANDROID_ID)
}
suspend fun saveImageFromUrl(url: String, imageDao: ImageDao,context: Context) {
    val existingImage = imageDao.getImageByUrl(url)
    if (existingImage == null) {
        withContext(Dispatchers.IO) {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        val stream = ByteArrayOutputStream()
                        resource.compress(Bitmap.CompressFormat.PNG, 100, stream) // یا JPEG
                        val imageData = stream.toByteArray()
                        GlobalScope.launch {

                            imageDao.insert(ImageEntity(imageUrl = url, imageData = imageData))
                        }

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
    } else {
        Log.e("APP", "Image already exists in the database.")
    }
}