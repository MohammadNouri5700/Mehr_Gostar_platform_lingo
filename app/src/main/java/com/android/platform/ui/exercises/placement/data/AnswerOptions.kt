package com.android.platform.ui.exercises.placement.data

import com.google.gson.annotations.SerializedName

data class AnswerOptions (


    @SerializedName("Answer"    ) var Answer    : String? = null,
    @SerializedName("IsCorrect" ) var IsCorrect : Int?    = null

)