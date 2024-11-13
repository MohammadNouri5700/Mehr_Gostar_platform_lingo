package com.android.platform.ui.course.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.LessonsReply
import com.android.platform.R
import com.android.platform.ui.course.list.CourseListViewModel
import com.android.platform.utils.ui.CircleProgressBar

class CourseListAdapter(val lessonsReply: LessonsReply,val viewModel: CourseListViewModel) : RecyclerView.Adapter<CourseListAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course_list, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val lesson = lessonsReply.getLessons(position)
        holder.name.text = lesson.title
        holder.lessonCount.text = lesson.exerciseCount.toString()
        holder.hour.text = (lesson.duration)
        holder.itemView.setOnClickListener{
            viewModel.openCourse(lesson.lessonId)
        }
    }

    override fun getItemCount(): Int = lessonsReply.lessonsCount

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.lblName)
        val lessonCount: TextView = itemView.findViewById(R.id.lbllesson)
        val mins: TextView = itemView.findViewById(R.id.lblMins)
        val hour: TextView = itemView.findViewById(R.id.lblhour)
        val percentage: TextView = itemView.findViewById(R.id.lblpercentage)
        val progressBar: CircleProgressBar = itemView.findViewById(R.id.progressbarCircle)
    }
}