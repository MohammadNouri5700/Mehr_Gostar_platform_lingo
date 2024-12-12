package com.android.platform.ui.exercises.listening

import androidx.lifecycle.ViewModel
import com.android.platform.ExerciseModel
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.SingleLiveEvent
import com.android.platform.ui.exercises.placement.data.PlacementEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class ListeningViewModel @Inject constructor(val call: CallQueueManager) : ViewModel() {


    lateinit var value: ExerciseModel
    lateinit var contentList: PlacementEntity
    var position: Int = 0
    private val gson = Gson()

    private val _event = SingleLiveEvent<String>()
    val event: SingleLiveEvent<String> get() = _event

    init {
        position=0
    }

    fun finishAudio() {
        _event.postValue("FinishAudio")
    }
    fun reloadList(){
        val contentListType = object : TypeToken<PlacementEntity>() {}.type
        contentList =
            gson.fromJson<PlacementEntity>(value.content.toString(), contentListType)
    }

    fun initList() {
        val contentListType = object : TypeToken<PlacementEntity>() {}.type
        contentList =
            gson.fromJson<PlacementEntity>(value.content.toString(), contentListType)
        _event.postValue("Init")
        initFirstQuestion()
    }

    fun initQuestion(position: Int){
        reloadList()
        if (position>=contentList.Questions.size){
            _event.postValue("FinishQuestion")
        }
        var tmp = contentList
        var i = -1
        val iterator = tmp.Questions.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            i++
            if (i == position) {
                continue
            } else iterator.remove()
        }
        contentList = tmp
        _event.postValue("Update")
    }

    fun initFirstQuestion() {
        initQuestion(0)
    }

    fun nextQuestion() {
        initQuestion(++position)
    }

    fun confirm() {
        _event.postValue("Confirm")
    }


}