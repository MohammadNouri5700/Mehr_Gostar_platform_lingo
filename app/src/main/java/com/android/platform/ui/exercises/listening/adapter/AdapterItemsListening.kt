package com.android.platform.ui.exercises.listening.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.R
import com.android.platform.ui.exercises.listening.ListeningViewModel
import com.android.platform.ui.exercises.listening.adapter.AdapterListening.ViewHolder
import com.android.platform.ui.exercises.placement.adapter.PlacementListAdapter.TaskViewHolder
import com.android.platform.ui.exercises.placement.data.AnswerOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class AdapterItemsListening (
    val items: ArrayList<AnswerOptions>,
    val viewModel: ListeningViewModel,
    val ctx: Context
): RecyclerView.Adapter<AdapterItemsListening.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterItemsListening.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_listening_item_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterItemsListening.ViewHolder, position: Int) {
        holder.name.text = items[position].Answer

        holder.itemView.setOnClickListener{
            viewModel.nextQuestion()
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.itemOrder)
    }

}