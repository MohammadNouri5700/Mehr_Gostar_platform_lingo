package com.android.platform.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableLiveData<String>().apply {
        value = "Home"
    }
    val event: LiveData<String> = _event


    fun openHome(){
        _event.postValue("Home")
    }
    fun openReport(){
        _event.postValue("Report")
    }
    fun openLeitner(){
        _event.postValue("Leitner")
    }
    fun openProfile(){
        _event.postValue("Profile")
    }

}