package com.android.platform.ui.exercises.ai_context.adapter

data class AiContextEntity(
    val Sentence: String,
    val Order: Int,
    val Type: MType
)

enum class MType{
    File,
    Text
}