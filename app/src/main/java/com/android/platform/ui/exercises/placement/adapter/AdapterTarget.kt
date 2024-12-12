package com.android.platform.ui.exercises.placement.adapter

import android.content.Context
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.R
import com.android.platform.ui.exercises.placement.PlacementViewModel
import com.android.platform.ui.exercises.placement.data.PlacementEntity
import com.android.platform.ui.exercises.placement.data.getSelected
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class AdapterTarget(
    var items: PlacementEntity,
    val viewModel: PlacementViewModel,
    val isSelected: Boolean,
    val ctx: Context
) : RecyclerView.Adapter<AdapterTarget.ViewHolder>() {



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.lblTitle)
        val recInTarget: RecyclerView = itemView.findViewById(R.id.recInTarget)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterTarget.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_placement_target_exercise, parent, false)

        view.findViewById<RecyclerView>(R.id.recInTarget).apply {
            layoutManager = FlexboxLayoutManager(parent.context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterTarget.ViewHolder, position: Int) {
        holder.textView.text = items.Questions[position].ContentQuestion


        holder.recInTarget.adapter =
            PlacementSelectedListAdapter(items.getSelected(position), this,viewModel, ctx)

    }


    override fun getItemCount(): Int = items.Questions.size

}