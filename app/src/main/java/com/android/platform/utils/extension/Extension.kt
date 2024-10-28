package com.android.platform.utils.extension

import android.app.Activity
import android.app.Fragment
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import com.android.platform.repository.data.database.ImageDao
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.ByteArrayOutputStream

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
fun Activity.showToast(message: Int) {
    Toast.makeText(this, resources.getText(message), Toast.LENGTH_SHORT).show()
}
fun Activity.setPage(name:String,className:String){
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
fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}