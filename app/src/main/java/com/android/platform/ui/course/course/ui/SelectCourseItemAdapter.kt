package com.android.platform.ui.course.course.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.ExerciseModel
import com.android.platform.LessonReply
import com.android.platform.LessonsReply
import com.android.platform.R
import com.android.platform.ui.course.course.CourseActivityViewModel
import com.android.platform.ui.course.list.CourseListViewModel
import com.android.platform.utils.ui.CircleProgressBar

class SelectCourseItemAdapter(
    private val exerciseModels: List<ExerciseModel>,
    private val viewModel: CourseActivityViewModel,
    private val context: Context
) : RecyclerView.Adapter<SelectCourseItemAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_course_dialog, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item:ExerciseModel = exerciseModels[position]
        holder.lblTitle.text = item.exerciseType.name

        holder.itemView.setOnClickListener{
            viewModel.loadExercise(item.id)
        }
    }

    override fun getItemCount(): Int = exerciseModels.size

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgExercise: ImageView = itemView.findViewById(R.id.imgExercise)
        val lblTitle: TextView = itemView.findViewById(R.id.lblTitle)
    }
}