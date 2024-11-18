package com.android.platform.di.factory

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.android.platform.R

class LoadingView(context: Context) : FrameLayout(context) {
    private var startTime: Long = 0
    private var minTime: Long = 500
    private var isLoadingVisible = false

    init {
        LayoutInflater.from(context).inflate(R.layout.view_loading, this, true)
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(context.getColor(android.R.color.transparent))
    }


    fun startTimer() {
        if (!isLoadingVisible) {
            startTime = System.currentTimeMillis()
            isLoadingVisible = true
        }
    }

    fun hideTimer(): Long {
        if (isLoadingVisible) {
            isLoadingVisible=false
            val elapsedTime = System.currentTimeMillis() - startTime
            return if (elapsedTime < minTime) {
                minTime - elapsedTime
            } else {
                0L
            }
        }else{
            return 0L
        }
    }

}