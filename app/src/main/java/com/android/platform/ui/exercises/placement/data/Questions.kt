package com.android.platform.ui.exercises.placement.data

import com.google.gson.annotations.SerializedName

data class Questions (

    @SerializedName("ContentQuestion" ) var ContentQuestion : String?                  = null,
    @SerializedName("AnswerOptions"   ) var AnswerOptions   : ArrayList<AnswerOptions> = arrayListOf()

)