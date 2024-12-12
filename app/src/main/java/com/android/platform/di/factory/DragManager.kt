package com.android.platform.di.factory

import android.content.ClipData
import android.graphics.Color
import android.graphics.Point
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DragManager(
    private val parentLayout: ViewGroup,
    private val topRecyclerView: RecyclerView,
    private val showHint: Boolean = false,
    var onItemDropped: ((View, Float, Float, Int?, Int?) -> Boolean)? = null
) {
    private var originalX: Float = 0f
    private var originalY: Float = 0f
    private var lastTargetView: View? = null // ذخیره آخرین ویویی که بر روی آن hover شده است
    private var draggedPosition: Int = -1

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_DOWN) {
                    val child = rv.findChildViewUnder(e.x, e.y)
                    if (child != null) {
                        draggedPosition =
                            rv.getChildAdapterPosition(child)  // ذخیره موقعیت ایتم درگ شده
                        startDrag(child)
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun startDrag(view: View) {
        originalX = view.left.toFloat()
        originalY = view.top.toFloat()

        val dragData = ClipData.newPlainText("", "")
        val dragShadow = object : View.DragShadowBuilder(view) {
            override fun onProvideShadowMetrics(outShadowSize: Point, outShadowTouchPoint: Point) {
                super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint)
                outShadowSize.set(view.width, view.height)
                outShadowTouchPoint.set(view.width / 2, view.height + (view.height))
            }
        }

        view.startDragAndDrop(
            dragData,
            dragShadow,
            view,
            0
        )


        parentLayout.setOnDragListener(DragEventListener(view))
    }

    private inner class DragEventListener(private val draggedView: View) : View.OnDragListener {

        override fun onDrag(v: View?, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    draggedView.alpha = 0f
                    return true
                }

                DragEvent.ACTION_DRAG_LOCATION -> {

                    draggedView.translationX = event.x - (draggedView.width / 2)
                    draggedView.translationY = event.y - (draggedView.height / 2)


                    val targetView = topRecyclerView.findChildViewUnder(
                        event.x,
                        event.y - (draggedView.height * 4) + draggedView.height
                    )

                    if (showHint && targetView != lastTargetView) {
                        lastTargetView?.setBackgroundColor(Color.TRANSPARENT) // Reset the last hovered item
                        targetView?.setBackgroundColor(Color.LTGRAY)
                    }
                    if (targetView != null) {
                        lastTargetView = targetView
                    }
                    return true

                }

                DragEvent.ACTION_DROP -> {
                    if (showHint) lastTargetView?.setBackgroundColor(Color.TRANSPARENT) // Reset on drop
                    val position =
                        if (lastTargetView != null) topRecyclerView.getChildAdapterPosition(
                            lastTargetView!!
                        ) else null
                    val isDroppedInValidArea = onItemDropped?.invoke(
                        draggedView,
                        event.x,
                        event.y,
                        position,
                        draggedPosition
                    ) ?: false
                    if (!isDroppedInValidArea) {
                        return true
                    }
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    draggedView.alpha = 0f
                    draggedView.animate()
                        .translationX(originalX - draggedView.left)
                        .translationY(originalY - draggedView.top)
                        .setDuration(0)
                        .start()
                    draggedView.animate().alpha(1f)
                        .setDuration(500)
                        .start()
                }
            }
            return true
        }
    }
}
