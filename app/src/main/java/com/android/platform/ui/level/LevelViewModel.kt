package com.android.platform.ui.level

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import javax.inject.Inject

class LevelViewModel  @Inject constructor() : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> get() = _text

    init {
        _text.value = "Loading"
    }


    private fun getLevels() {
//        val data = listOf(
//            Level("Beginner/Elementary","26 minutes 14 second",67),
//            Level("Pre Intermediate","29 minutes 10 second",12),
//            Level("Intermediate","24 minutes 5 second",0),
//            Level("Upper Intermediate","34 minutes 12 second",0),
//            Level("Advanced","12 minutes 54 second",0),
//            Level("C2 English (Proficient)","41 minutes 35 second",2),
//        )
    }






}