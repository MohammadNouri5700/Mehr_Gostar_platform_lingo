package com.android.platform.ui.level.levels

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.R
import com.android.platform.utils.ui.CircleProgressBar

class LevelAdapter(private val taskList: List<Level>, private val context: Context) : RecyclerView.Adapter<LevelAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_level, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.progressBar.setProgress(task.point)
        holder.percentage.text = task.point.toString()
    }

    override fun getItemCount(): Int = taskList.size

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.lblName)
        val mins: TextView = itemView.findViewById(R.id.lblMins)
        val percentage: TextView = itemView.findViewById(R.id.lblpercentage)
        val progressBar: CircleProgressBar = itemView.findViewById(R.id.progressbarCircle)
    }
}