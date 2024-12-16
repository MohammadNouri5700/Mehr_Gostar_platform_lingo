package com.android.platform.ui.global

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.platform.ExerciseModel
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.SingleLiveEvent
import com.android.platform.repository.data.model.BotMessageEntity
import com.android.platform.ui.exercises.ai_context.adapter.AiContextEntity
import com.android.platform.ui.exercises.ai_context.adapter.MType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import java.io.File
import javax.inject.Inject

class BotChatViewModel @Inject constructor(val call: CallQueueManager) : ViewModel() {


    lateinit var value: ExerciseModel
    lateinit var data: ArrayList<AiContextEntity>
    var messageList: ArrayList<BotMessageEntity> = arrayListOf()
    private val gson = Gson()

    var messageIndex = 0;


    private val _event = SingleLiveEvent<String>()
    val eventBot: SingleLiveEvent<String> get() = _event

    private val _messageUser = SingleLiveEvent<String>()
    val messageUser: SingleLiveEvent<String> get() = _messageUser

    fun initList() {
        val contentListType = object : TypeToken<ArrayList<AiContextEntity>>() {}.type
        data = gson.fromJson<ArrayList<AiContextEntity>>(value.content.toString(), contentListType)
        initFirstMessages()
        _event.postValue("Init")
    }

    private fun initFirstMessages() {
        messageList.add(
            BotMessageEntity(
                data[messageIndex].Type,
                data[messageIndex].Sentence,
                "",
                "",
                false,
                false
            )
        )

        messageIndex++
        messageList.add(
            BotMessageEntity(
                data[messageIndex].Type,
                data[messageIndex].Sentence,
                "",
                "",
                true,
                true
            )
        )
    }

    private fun addNextMessage() {
        messageIndex++

        if (messageIndex == data.size) {
            confirm()
            return
        }

        messageList.add(
            BotMessageEntity(
                data[messageIndex].Type,
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
                data[messageIndex].Type,
                data[messageIndex].Sentence,
                "",
                "",
                true,
                true
            )
        )
    }

    private fun addLastMessage() {
        messageList.add(
            BotMessageEntity(
                data[messageIndex].Type,
                data[messageIndex].Sentence,
                "",
                "",
                true,
                true
            )
        )
    }

    private fun addMessages(value: String, type: MType) {
        messageList.add(BotMessageEntity(type, value, "", "", true))
    }

    private fun clearMessage() {
        _event.postValue("ClearMessage")
    }

    private fun setUserMessage(value: String) {
        _messageUser.postValue(value)
    }

    private fun scrollToEnd() {
        _event.postValue("ScrollToEnd")
    }

    private fun removeMessage(value: BotMessageEntity) {

        val iterator = messageList.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.message == value.message) {
                iterator.remove()
            }
        }

    }

    private fun updateRemoveList() {
        _event.postValue("UpdateRemove")
    }

    private fun updateList() {
        _event.postValue("Update")
    }

    private fun confirm() {
        _event.postValue("Confirm")
    }

    private fun refreshNewData() {
        updateList()
        scrollToEnd()
        clearMessage()
    }

    fun sendMessage(value: String) {
        call.enqueueMainTask {
            removeMessage(messageList.last())
            updateRemoveList()
            delay(100)
            addMessages(value, MType.Text)
            refreshNewData()

            delay(1000)
            processMessage(value)
        }
    }

    fun sendVoice(filePath: String, transcribe: String) {

        call.enqueueMainTask {
            Log.e("APP","WE REMOVE ${messageList.last().message} ")
            removeMessage(messageList.last())
            updateRemoveList()
            delay(100)
            addMessages(filePath, MType.File)
            refreshNewData()

//            delay(1000)
//            processVoiceMessage(transcribe)
        }

    }

    private fun processVoiceMessage(transcribe: String) {
        if (checkMessage(transcribe)) {
            call.enqueueMainTask {
                addNextMessage()
                delay(100)
                refreshNewData()
            }
        } else {
            call.enqueueMainTask {
//                removeMessage(messageList.last())
//                updateRemoveList()
//                delay(100)
//                addLastMessage()
                refreshNewData()
                setUserMessage(transcribe)
            }
        }

    }

    private fun processMessage(value: String) {
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
                setUserMessage(value)
            }
        }

    }

    private fun checkMessage(value: String): Boolean {
        if (value.trim() == data[messageIndex].Sentence.trim()) return true else return false
    }

    override fun onCleared() {
        super.onCleared()
        messageList.clear()
        messageIndex = 0
        data.clear()

    }
}