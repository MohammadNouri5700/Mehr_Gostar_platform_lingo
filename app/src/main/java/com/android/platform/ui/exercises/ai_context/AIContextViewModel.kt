package com.android.platform.ui.exercises.ai_context

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.ExerciseModel
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.SingleLiveEvent
import com.android.platform.ui.exercises.ai_context.adapter.AiContextEntity
import com.android.platform.ui.exercises.order.adapter.OrderEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class AIContextViewModel @Inject constructor( val call: CallQueueManager) : ViewModel() {

    lateinit var value: ExerciseModel
    var messageList: ArrayList<AiContextEntity> = arrayListOf()
    private val gson = Gson()

    private val _event = SingleLiveEvent<String>()
    val event: SingleLiveEvent<String> get() = _event


    fun initList() {
        val contentListType = object : TypeToken<ArrayList<AiContextEntity>>() {}.type
        messageList = gson.fromJson<ArrayList<AiContextEntity>>(value.content.toString(), contentListType)
        _event.postValue("Init")

    }

    private fun addMessages(value: String) {
        messageList.add(AiContextEntity(value, messageList.size + 1))
    }
    private fun clearMessage(){
        _event.postValue("ClearMessage")
    }
    private fun scrollToEnd() {
        _event.postValue("ScrollToEnd")
    }
    fun removeMessage(value: AiContextEntity) {
        messageList.remove(value)
        messageList.add(value)
    }

    fun updateList() {
        _event.postValue("Update")
    }

    fun confirm() {
        _event.postValue("Confirm")
    }

    fun sendMessage(value: String) {
        if (value=="") return
        addMessages(value)
        updateList()
        clearMessage()
        scrollToEnd()
    }

}