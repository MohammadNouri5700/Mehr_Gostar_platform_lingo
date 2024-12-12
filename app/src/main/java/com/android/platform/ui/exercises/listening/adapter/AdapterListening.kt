package com.android.platform.ui.exercises.listening.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.R
import com.android.platform.ui.exercises.listening.ListeningViewModel
import com.android.platform.ui.exercises.placement.adapter.AdapterTarget
import com.android.platform.ui.exercises.placement.adapter.AdapterTarget.ViewHolder
import com.android.platform.ui.exercises.placement.adapter.PlacementSelectedListAdapter
import com.android.platform.ui.exercises.placement.data.PlacementEntity
import com.android.platform.ui.exercises.placement.data.getSelected
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class AdapterListening (
    var items: PlacementEntity,
    val viewModel: ListeningViewModel,
    val ctx: Context
): RecyclerView.Adapter<AdapterListening.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_listening_exercise, parent, false)

        view.findViewById<RecyclerView>(R.id.recItems).apply {
            layoutManager = FlexboxLayoutManager(parent.context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.CENTER
            }
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.textView.text = items.Questions[position].ContentQuestion
        holder.recInTarget.adapter =
            AdapterItemsListening(items.Questions[position].AnswerOptions, viewModel, ctx)

    }

    override fun getItemCount(): Int = items.Questions.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.lblQuestion)
        val recInTarget: RecyclerView = itemView.findViewById(R.id.recItems)
    }

}