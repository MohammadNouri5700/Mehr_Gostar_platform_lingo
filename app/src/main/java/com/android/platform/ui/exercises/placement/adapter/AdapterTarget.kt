package com.android.platform.ui.exercises.placement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.R
import com.android.platform.ui.exercises.placement.PlacementViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class AdapterTarget (private val items: List<PlacementEntity>, val viewModel: PlacementViewModel, val isSelected:Boolean, val ctx: Context) : RecyclerView.Adapter<AdapterTarget.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.lblTitle)
        val recInTarget: RecyclerView = itemView.findViewById(R.id.recInTarget)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterTarget.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_placement_target_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterTarget.ViewHolder, position: Int) {
        holder.textView.text = items[position].Title

        val flexboxLayoutManager = FlexboxLayoutManager(ctx).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        holder.recInTarget.layoutManager = flexboxLayoutManager
        holder.recInTarget.adapter = PlacementListAdapter(items[position].getSelected(), viewModel, ctx)
    }

    override fun getItemCount(): Int = items.size


    fun addTOItem(item: String) {

    }


}