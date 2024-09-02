package com.android.platform.ui.learn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LearnViewModel  @Inject constructor() : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> get() = _text

    init {
        _text.value = "Hello from ViewModel!"
    }






}