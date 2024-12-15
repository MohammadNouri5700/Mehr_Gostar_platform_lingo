package com.android.platform.repository.data.model

data class BotMessageEntity (
    val type: MType,
    val message: String,
    val data: String,
    val result: String,
    val sideUs: Boolean,
    var fade: Boolean=false
)

enum class MType{
    voiceLocal,
    voiceOver,
    messageText
}