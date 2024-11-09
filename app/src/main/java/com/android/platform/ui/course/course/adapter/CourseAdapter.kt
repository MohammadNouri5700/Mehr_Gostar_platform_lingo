package com.android.platform.ui.course.course.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        when (position) {
            0 -> {
                holder.name.text = context.resources.getText(R.string.vocabulary)
                holder.lblMins.text = " 15 "
                holder.imgLesson.apply {
                    alpha = 0f; setImageResource(R.drawable.vocab_course)
                }.animate().alpha(1f).setDuration(700).start()
                holder.itemView.setOnClickListener{
                    viewModel.loadItem(lessonsReply.vocabulary.id)
                }
            }

            1 -> {
                holder.name.text = context.resources.getText(R.string.speaking)
                holder.lblMins.text = " 15 "
                holder.imgLesson.apply {
                    alpha = 0f; setImageResource(R.drawable.speaking_course)
                }.animate().alpha(1f).setDuration(700).start()
                holder.itemView.setOnClickListener{
                    viewModel.loadItem(lessonsReply.speaking.id)
                }
            }

            2 -> {
                holder.name.text = context.resources.getText(R.string.listening)
                holder.lblMins.text = "  TODO  "
                holder.imgLesson.apply {
                    alpha = 0f; setImageResource(R.drawable.listening_course)
                }.animate().alpha(1f).setDuration(700).start()
//                holder.itemView.setOnClickListener{
//                    viewModel.loadItem(lessonsReply..id)
//                }
            }

            3 -> {
                holder.name.text = context.resources.getText(R.string.grammar)
                holder.lblMins.text = " 15 "
                holder.imgLesson.apply {
                    alpha = 0f; setImageResource(R.drawable.grammer_course)
                }.animate().alpha(1f).setDuration(700).start()
                holder.itemView.setOnClickListener{
                    viewModel.loadItem(lessonsReply.grammer.id)
                }
            }
            4 -> {
                holder.name.text = context.resources.getText(R.string.combinationexercise)
                holder.lblMins.text = " 15 "
                holder.imgLesson.apply {
                    alpha = 0f; setImageResource(R.drawable.combination_course)
                }.animate().alpha(1f).setDuration(700).start()
//                holder.itemView.setOnClickListener{
//                    viewModel.loadItem(lessonsReply.grammer.id)
//                }
            }
        }

//        holder.name.text = level.title
//
//        holder.itemView.setOnClickListener{
//            viewModel.openCourse(level.lessonId)
//        }
    }

    override fun getItemCount(): Int = 4

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLesson: ImageView = itemView.findViewById(R.id.imgLesson)
        val name: TextView = itemView.findViewById(R.id.lblName)
        val lblMins: TextView = itemView.findViewById(R.id.lblMins)
    }
}