package com.android.platform.ui.exercises.ai_context.adapter

import androidx.recyclerview.widget.DiffUtil

class AiContextDiffCallback(
    private val oldList: List<AiContextEntity>,
    private val newList: List<AiContextEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]

    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}