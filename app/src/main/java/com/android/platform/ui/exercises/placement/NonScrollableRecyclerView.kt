package com.android.platform.ui.exercises.placement

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class NonScrollableRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        return false
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        return false
    }

    override fun scrollToPosition(position: Int) {
    }

    override fun smoothScrollToPosition(position: Int) {
    }
}