package com.android.platform.ui.course.course.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.ContentResult
import com.android.platform.LessonReply
import com.android.platform.LessonsReply
import com.android.platform.R
import com.android.platform.ui.course.course.CourseActivityViewModel
import com.android.platform.ui.course.list.CourseListViewModel
import com.android.platform.utils.ui.CircleProgressBar

class CourseAdapter(
    private val lessonsReply: LessonReply,
    private val viewModel: CourseActivityViewModel,
    private val context: Context
) : RecyclerView.Adapter<CourseAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var item = lessonsReply.getContents(position)

        when (item.contentType.name) {
            "Vocabulary" -> {
                holder.name.text = item.contentType.name
                holder.lblLessons.text = item.exercisesCount.toString()
                holder.imgLesson.apply {
                    alpha = 0f; setImageResource(R.drawable.vocab_course)
                }.animate().alpha(1f).setDuration(700).start()
                holder.itemView.setOnClickListener{
                    viewModel.loadItem(item.contentType.number)
                }
            }

            "Speaking" -> {
                holder.name.text = item.contentType.name
                holder.lblLessons.text = item.exercisesCount.toString()
                holder.imgLesson.apply {
                    alpha = 0f; setImageResource(R.drawable.speaking_course)
                }.animate().alpha(1f).setDuration(700).start()
                holder.itemView.setOnClickListener{
                    viewModel.loadItem(item.contentType.number)
                }
            }

            "Listening" -> {
                holder.name.text = item.contentType.name
                holder.lblLessons.text = item.exercisesCount.toString()
                holder.imgLesson.apply {
                    alpha = 0f; setImageResource(R.drawable.listening_course)
                }.animate().alpha(1f).setDuration(700).start()
                holder.itemView.setOnClickListener{
                    viewModel.loadItem(item.contentType.number)
                }
            }

            "Grammar" -> {
                holder.name.text = item.contentType.name
                holder.lblLessons.text = item.exercisesCount.toString()
                holder.imgLesson.apply {
                    alpha = 0f; setImageResource(R.drawable.grammer_course)
                }.animate().alpha(1f).setDuration(700).start()
                holder.itemView.setOnClickListener{
                    viewModel.loadItem(item.contentType.number)
                }
            }
            "CombinationExercise" -> {
                holder.name.text = item.contentType.name
                holder.lblLessons.text = item.exercisesCount.toString()
                holder.imgLesson.apply {
                    alpha = 0f; setImageResource(R.drawable.combination_course)
                }.animate().alpha(1f).setDuration(700).start()
                holder.itemView.setOnClickListener{
                    viewModel.loadItem(item.contentType.number)
                }
            }
        }

    }

    override fun getItemCount(): Int = lessonsReply.contentsCount-1

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLesson: ImageView = itemView.findViewById(R.id.imgLesson)
        val name: TextView = itemView.findViewById(R.id.lblName)
        val lblLessons: TextView = itemView.findViewById(R.id.lblLessons)
    }
}