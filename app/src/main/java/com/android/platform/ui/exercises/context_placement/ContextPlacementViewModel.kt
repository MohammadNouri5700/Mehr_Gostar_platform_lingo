package com.android.platform.ui.exercises.context_placement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.platform.ExerciseModel
import javax.inject.Inject

class ContextPlacementViewModel @Inject constructor() : ViewModel() {


//    # com.android.platform.ExerciseModel@7233a1a2
//    content: "<p>It is #important# to teach children at a level that matches their #existing# skills and level. Lessons that are too easy can bore #students# and lessons that are too hard can mean students lose #confidence#. The Cambridge English Placement Test for Pre A1 Starters, A1 Movers and A2 Flyers is an adaptive test that is designed to assess a child\342\200\231s ability and give #valuable# information to teachers and parents.</p>"
//    content_type: Vocabulary
//    content_type_value: 1
//    exercise_type: ContextPlacement
//    exercise_type_value: 3
//    id: 16

    lateinit var value: ExerciseModel


    var items: MutableList<String> = mutableListOf<String>()
    var itemsBackup: MutableList<String> = mutableListOf<String>()
    var sentences: MutableList<String> = mutableListOf<String>()
    var selectSentences: MutableList<Pair<Boolean, String>> = arrayListOf()


    private val _event = MutableLiveData<String>()
    val event: LiveData<String> get() = _event


    fun initList() {
        val result = extractWords(value.content)
        items.addAll(result.first)
        itemsBackup.addAll(result.first)
        sentences.addAll(result.second)
        fillSelected()
        _event.postValue("Init")
    }


    private fun addGap(): String {
        return ("_ _ _ _ _")
    }

    private fun fillSelected() {
        sentences.forEach { it ->
            selectSentences.add(Pair<Boolean, String>(it.trim() == addGap(), addGap()))
        }
    }

    fun updateLists() {
        sentences.forEachIndexed { index, it ->
            if (selectSentences[index].first == true) {
                sentences[index] = selectSentences[index].second
            }
        }
        items.clear()
        items.addAll(itemsBackup)
        selectSentences.forEachIndexed { index, it ->
            if (selectSentences[index].first == true) {
                val iterator = items.iterator()
                while (iterator.hasNext()) {
                    val item = iterator.next()
                    if (item == selectSentences[index].second) {
                        iterator.remove()
                    }
                }
            }
        }

        _event.postValue("Update")

    }

    fun confirm() {
        _event.postValue("Confirm")
    }

    fun addSelected(position: Int, value: String): Boolean {
        if (position<=0) return false

        if (selectSentences[position].first == true) {
            if (selectSentences[position].second.trim() == addGap())
                selectSentences[position] = Pair<Boolean, String>(true, value)
            else {
                items.add(selectSentences[position].second)
                selectSentences[position] = Pair<Boolean, String>(true, value)
            }
            updateLists()
            return true
        } else {
            return false
        }
    }

    private fun extractWords(
        input: String,
        sizePerLine: Int = 0
    ): Pair<ArrayList<String>, ArrayList<String>> {
        val regex = "#(.*?)#"
        var st = 0
        val wordsList = ArrayList<String>()
        val sentencesList = ArrayList<String>()
        val matches = Regex(regex).findAll(input)

        matches.forEach {
            val range = it.groups[0]?.range
            if (range != null) {
                var beforeHashText = input.substring(st, range.first)
                if (beforeHashText.isNotEmpty()) {
                    beforeHashText += addGap()
                    val words = splitTextIntoWordsWithSpaces(beforeHashText).map { "$it " }
                    sentencesList.addAll(words)
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

    fun splitTextIntoWordsWithSpaces(text: String): List<String> {
        val textWithPlaceholders = text.replace(addGap(), "____PLACEHOLDER____")
        val words = textWithPlaceholders.split(" ")
        return words.map { it.replace("____PLACEHOLDER____", addGap()) }
    }
}