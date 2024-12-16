package com.android.platform.repository.data.model

import com.android.platform.ui.exercises.ai_context.adapter.MType

data class BotMessageEntity (
    val type: MType,
    val message: String,
    val data: String,
    val result: String,
    val sideUs: Boolean,
    var fade: Boolean = false,
    val id: Long = BotMessageEntity.generateId()
) {
    companion object {
        private var currentId: Long = 0

        private fun generateId(): Long {
            currentId++
            return currentId
        }
    }
}
