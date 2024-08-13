package com.android.platform.utils.extension

import android.app.Activity
import android.widget.Toast

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
fun Activity.showToast(message: Int) {
    Toast.makeText(this, resources.getText(message), Toast.LENGTH_SHORT).show()
}
