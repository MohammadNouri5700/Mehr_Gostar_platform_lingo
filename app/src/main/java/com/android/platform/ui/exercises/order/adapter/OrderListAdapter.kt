package com.android.platform.ui.exercises.order.adapter

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
import com.android.platform.utils.ui.CircleProgressBar

class OrderListAdapter(private val items: List<OrderEntity>, val viewModel: OrderViewModel, val isSelected:Boolean,val ctx: Context) : RecyclerView.Adapter<OrderListAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_exercise, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.name.text = items[position].Sentence

        holder.itemView.setOnClickListener{
            if (!isSelected)
                viewModel.addSelected(items[position])
            else
                viewModel.removeSelected(items[position])
        }

        if (isSelected){
            holder.name.setCompoundDrawablesRelativeWithIntrinsicBounds(ctx.resources.getDrawable( R.drawable.incorrect), null, null, null)
        }

    }

    override fun getItemCount(): Int = items.size

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.itemOrder)
    }
}