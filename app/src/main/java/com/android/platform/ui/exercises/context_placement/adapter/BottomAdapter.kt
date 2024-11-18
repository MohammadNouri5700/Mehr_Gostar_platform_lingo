package com.android.platform.ui.exercises.context_placement.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.R

class BottomAdapter(private val words: List<String>, private val onWordMoved: (String) -> Unit) :
    RecyclerView.Adapter<BottomAdapter.WordViewHolder>() {

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.wordText)

        init {
            // برای درگ کردن کلمات
            itemView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val draggedWord = words[adapterPosition]
                    onWordMoved(draggedWord)
                }
                false
            }
        }

        fun bind(word: String) {
            textView.text = word
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.textView.text = words[position]

    }

    override fun getItemCount(): Int = words.size
}