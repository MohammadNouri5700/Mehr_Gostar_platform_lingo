package com.android.platform.ui.exercises.placement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.ExerciseModel
import com.android.platform.LessonsReply
import com.android.platform.R
import com.android.platform.ui.course.list.CourseListViewModel
import com.android.platform.ui.exercises.order.OrderViewModel
import com.android.platform.ui.exercises.placement.PlacementViewModel
import com.android.platform.utils.ui.CircleProgressBar

class PlacementListAdapter(private val items: ArrayList<String>, val viewModel: PlacementViewModel, val ctx: Context) : RecyclerView.Adapter<PlacementListAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_placement_exercise, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.name.text = items[position]
    }

    override fun getItemCount(): Int = items.size

    fun removeItem(position: Int): String {
        val item = items.removeAt(position)
        notifyItemRemoved(position)
        return item
    }

    fun addItem(item: String) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.itemOrder)
    }
}