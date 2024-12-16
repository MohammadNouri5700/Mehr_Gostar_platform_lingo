package com.android.platform.ui.global

import androidx.recyclerview.widget.DiffUtil
import com.android.platform.repository.data.model.BotMessageEntity

class AiBotDiffCallback(
    private val oldList: List<BotMessageEntity>,
    private val newList: List<BotMessageEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id

    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}