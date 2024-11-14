package com.android.platform.ui.exercises.placement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.ExerciseModel
import com.android.platform.ui.exercises.order.adapter.OrderEntity
import com.android.platform.ui.exercises.placement.adapter.PlacementEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class PlacementViewModel @Inject constructor(): ViewModel() {

    lateinit var value: ExerciseModel
    lateinit var contentList:ArrayList<PlacementEntity>
    private val gson = Gson()

    private val _event = MutableLiveData<String>()
    val event: LiveData<String> get() = _event


    fun initList(){
        val contentListType = object : TypeToken<ArrayList<PlacementEntity>>() {}.type
        contentList = gson.fromJson<ArrayList<PlacementEntity>>(value.content.toString(), contentListType)
        _event.postValue("Init")
    }

    fun updateList() {
        _event.postValue("Update")
    }


}