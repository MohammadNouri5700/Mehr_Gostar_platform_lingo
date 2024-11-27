package com.android.platform.repository.data.model

data class BotMessageEntity (
    val type: MType,
    val message: String,
    val data: String,
    val result: String
)

enum class MType{
    voiceLocal,
    voiceOver,
    messageText
}