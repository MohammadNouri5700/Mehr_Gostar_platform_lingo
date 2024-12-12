package com.android.platform.ui.exercises.context_placement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.R

class TopAdapter (val words: MutableList<String>,val ctx: Context): RecyclerView.Adapter<TopAdapter.WordViewHolder>() {

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.wordText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_context_placement_sentence_exercise, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {

        holder.textView.text = words[position]
//        calculateCharactersPerLine(holder.textView)
    }

    override fun getItemCount(): Int = words.size



}