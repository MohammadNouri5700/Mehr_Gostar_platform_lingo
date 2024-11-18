package com.android.platform.ui.exercises.ai_context.adapter

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class SlideInItemAnimator : DefaultItemAnimator() {
    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.translationX = holder.itemView.width.toFloat()
        holder.itemView.animate()
            .translationX(0f)
            .setDuration(500)
            .start()
        return super.animateAdd(holder)
    }
}