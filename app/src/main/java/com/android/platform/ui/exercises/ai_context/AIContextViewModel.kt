package com.android.platform.ui.exercises.ai_context

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.ExerciseModel
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.SingleLiveEvent
import com.android.platform.repository.data.model.BotMessageEntity
import com.android.platform.repository.data.model.MType
import com.android.platform.ui.exercises.ai_context.adapter.AiContextEntity
import com.android.platform.ui.exercises.order.adapter.OrderEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AIContextViewModel @Inject constructor(val call: CallQueueManager) : ViewModel() {

    lateinit var value: ExerciseModel
    lateinit var data: ArrayList<AiContextEntity>
    var messageList: ArrayList<BotMessageEntity> = arrayListOf()
    var messageListData: ArrayList<BotMessageEntity> = arrayListOf()
    private val gson = Gson()

    private val _event = SingleLiveEvent<String>()
    val event: SingleLiveEvent<String> get() = _event


    var messageIndex = 0;


    fun initList() {
        val contentListType = object : TypeToken<ArrayList<AiContextEntity>>() {}.type
        data = gson.fromJson<ArrayList<AiContextEntity>>(value.content.toString(), contentListType)
        initFirstMessages()
        _event.postValue("Init")
    }

    fun initFirstMessages() {
        messageList.add(
            BotMessageEntity(
                MType.messageText,
                data[messageIndex].Sentence,
                "",
                "",
                false,
                true
            )
        )
        messageIndex++
        messageList.add(
            BotMessageEntity(
                MType.messageText,
                data[messageIndex].Sentence,
                "",
                "",
                true,
                true
            )
        )
    }

    fun addNextMessage() {
        messageIndex++

        if (messageIndex == data.size){
            confirm()
            return
        }

        messageList.add(
            BotMessageEntity(
                MType.messageText,
                data[messageIndex].Sentence,
                "",
                "",
                false,
                true
            )
        )
        messageIndex++
        messageList.add(
            BotMessageEntity(
                MType.messageText,
                data[messageIndex].Sentence,
                "",
                "",
                true,
                true
            )
        )
    }
    fun addLastMessage(){
        messageList.add(
            BotMessageEntity(
                MType.messageText,
                data[messageIndex].Sentence,
                "",
                "",
                true,
                true
            )
        )
    }

    private fun addMessages(value: String) {
        messageList.add(BotMessageEntity(MType.messageText, value, "", "", true))
    }

    private fun clearMessage() {
        _event.postValue("ClearMessage")
    }

    private fun scrollToEnd() {
        _event.postValue("ScrollToEnd")
    }

    fun removeMessage(value: BotMessageEntity) {
        messageList.remove(value)
    }

    fun updateRemoveList() {
        _event.postValue("UpdateRemove")
    }

    fun updateList() {
        _event.postValue("Update")
    }

    fun confirm() {
        _event.postValue("Confirm")
    }

    fun refreshNewData() {
        updateList()
        scrollToEnd()
        clearMessage()
    }

    fun sendMessage(value: String) {
        call.enqueueMainTask {
            removeMessage(messageList.last())
            updateRemoveList()
            delay(100)
            addMessages(value)
            refreshNewData()

            delay(1000)
            processMessage(value)
        }
    }

    fun processMessage(value: String) {


        if (checkMessage(value)) {
            call.enqueueMainTask {
                addNextMessage()
                delay(100)
                refreshNewData()
            }
        } else {
            call.enqueueMainTask {
                removeMessage(messageList.last())
                updateRemoveList()
                delay(100)
                addLastMessage()
                refreshNewData()
            }
        }

    }

    // I am fine thanks, How about you ?
    // Somehow, it depend!

    fun checkMessage(value: String): Boolean {
        if (value.trim() == data[messageIndex].Sentence.trim()) return true else return false
    }

}