package com.android.platform.ui.exercises.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.ExerciseModel
import com.android.platform.ui.exercises.order.adapter.OrderEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class OrderViewModel @Inject constructor(): ViewModel() {

    lateinit var value: ExerciseModel
    lateinit var contentList:ArrayList<OrderEntity>
    lateinit var selectedList:ArrayList<OrderEntity>
    private val gson = Gson()

    private val _event = MutableLiveData<String>()
    val event: LiveData<String> get() = _event


    fun initList(){
        val contentListType = object : TypeToken<ArrayList<OrderEntity>>() {}.type
        contentList = gson.fromJson<ArrayList<OrderEntity>>(value.content.toString(), contentListType)
        selectedList = arrayListOf()
        _event.postValue("Init")

    }
    fun addSelected(value: OrderEntity){
        selectedList.add(value)
        contentList.remove(value)
        _event.postValue("Update")
    }
    fun removeSelected(value: OrderEntity){
        selectedList.remove(value)
        contentList.add(value)
        _event.postValue("Update")
    }
    fun updateList(){
        _event.postValue("Update")
    }
    fun confirm(){
        if (contentList.size==0){
            _event.postValue("Confirm")
        }
    }
}