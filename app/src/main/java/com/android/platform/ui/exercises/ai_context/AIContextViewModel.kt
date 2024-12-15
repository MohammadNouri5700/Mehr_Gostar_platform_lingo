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
    var messageListBK: ArrayList<BotMessageEntity> = arrayListOf()
    private val gson = Gson()

    private val _event = SingleLiveEvent<String>()
    val event: SingleLiveEvent<String> get() = _event


    var safePosition = 1


    fun initList() {
        val contentListType = object : TypeToken<ArrayList<AiContextEntity>>() {}.type
        data = gson.fromJson<ArrayList<AiContextEntity>>(value.content.toString(), contentListType)
        nextMessage()
        _event.postValue("Init")
    }

    fun nextMessage() {
        if (safePosition>data.size){
            event.postValue("Confirm")
            return
        }
        messageListBK.addAll(messageList)
        messageList.clear()
        messageListBK.forEachIndexed { index, item ->
            item.fade=false
            messageList.add(item)
        }
        messageListBK.clear()
        messageList.add(
            BotMessageEntity(
                MType.messageText,
                data[safePosition - 1].Sentence,
                "",
                "",
                false
            )
        )
        messageList.add(
            BotMessageEntity(
                MType.messageText,
                data[safePosition].Sentence,
                "",
                "",
                true,
                true
            )
        )

        safePosition+=2
    }

    private fun addMessages(value: String): Boolean {
        if (value.trim() == messageList.last().message.trim()) {
            messageList.add(BotMessageEntity(MType.messageText, value, "", "", true))
            return true
        } else
            messageList.add(BotMessageEntity(MType.messageText, value, "", "", true))
        return false
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

    fun refreshMessage() {
        updateList()
        clearMessage()
    }

    fun sendMessage(value: String) {
        if (value == "") return
        val res = addMessages(value)
        refreshMessage()

        if (!res) {
            call.enqueueIoTask {
                withContext(Dispatchers.Main) {
                    messageList.add(messageList[messageList.size-3])
                    messageList.add(messageList[messageList.size-3])
                    refreshMessage()
                }
            }
        } else {
            nextMessage()
        }

        call.enqueueMainTask {
            delay(200)
            scrollToEnd()
        }
    }

}