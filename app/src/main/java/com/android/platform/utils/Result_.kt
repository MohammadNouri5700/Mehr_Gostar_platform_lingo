package com.android.platform.utils

sealed class Result_<out T> {
    data class Success<out T>(val data: T) : Result_<T>()
    data class Error(val exception: Exception) : Result_<Nothing>()
    object Loading : Result_<Nothing>()
}