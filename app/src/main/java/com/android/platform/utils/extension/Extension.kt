package com.android.platform.utils.extension

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
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