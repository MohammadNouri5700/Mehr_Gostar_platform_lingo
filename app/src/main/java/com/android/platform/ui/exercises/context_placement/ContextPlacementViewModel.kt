package com.android.platform.ui.exercises.context_placement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.ExerciseModel
import com.android.platform.ui.exercises.order.adapter.OrderEntity
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class ContextPlacementViewModel @Inject constructor(): ViewModel() {


//    # com.android.platform.ExerciseModel@7233a1a2
//    content: "<p>It is #important# to teach children at a level that matches their #existing# skills and level. Lessons that are too easy can bore #students# and lessons that are too hard can mean students lose #confidence#. The Cambridge English Placement Test for Pre A1 Starters, A1 Movers and A2 Flyers is an adaptive test that is designed to assess a child\342\200\231s ability and give #valuable# information to teachers and parents.</p>"
//    content_type: Vocabulary
//    content_type_value: 1
//    exercise_type: ContextPlacement
//    exercise_type_value: 3
//    id: 16

    lateinit var value: ExerciseModel

    var items: ArrayList<String> = arrayListOf()
    var sentences: ArrayList<String> = arrayListOf()
    var selectSentences: ArrayList<String> = arrayListOf()




    private val _event = MutableLiveData<String>()
    val event: LiveData<String> get() = _event



    fun initList() {
        val result = extractWords(value.content)
        items.addAll(result.first)
        sentences.addAll(result.second)
        fillSelected()
        _event.postValue("Init")
    }

    fun generateSentence():String{
        var data= ""
        sentences.forEachIndexed{index, item ->
            data+=item
            if (index==sentences.size-1) return data
            data += if (selectSentences[index] == items[index]){
                items[index]
            }else{
                addGap()
            }
        }
        return data
    }

    private fun addGap():String{
       return (" _ _ _ _ _ _ ")
    }

    private fun fillSelected(){
        items.forEach { _ ->  selectSentences.add(addGap()) }
    }

    private fun extractWords(input: String): Pair<ArrayList<String>, ArrayList<String>> {
        val regex = "#(.*?)#"
        var st = 0
        val wordsList = ArrayList<String>()
        val sentencesList = ArrayList<String>()
        val matches = Regex(regex).findAll(input)

        matches.forEach {
            val range = it.groups[0]?.range
            if (range != null) {
                val beforeHashText = input.substring(st, range.first)
                if (beforeHashText.isNotEmpty()) {
                    sentencesList.add(beforeHashText)
                }
                st = range.last + 1
            }
            it.groups[1]?.let { word ->
                wordsList.add(word.value)
            }
        }

        val remainingText = input.substring(st)
        if (remainingText.isNotEmpty()) {
            sentencesList.add(remainingText)
        }

        return Pair(wordsList, sentencesList)
    }
}