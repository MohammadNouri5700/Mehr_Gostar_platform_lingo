package com.android.platform.ui.course.course.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.ExerciseModel
import com.android.platform.R
import com.android.platform.ui.course.course.CourseActivityViewModel
import com.android.platform.utils.extension.animateFadeDown
import com.android.platform.utils.extension.animateFadeUp
import com.android.platform.utils.extension.animateLeft
import com.android.platform.utils.extension.animateRight
import com.google.android.flexbox.FlexboxLayoutManager


class SelectCourseItemAdapter(
    private val exerciseModels: List<ExerciseModel>,
    private val viewModel: CourseActivityViewModel,
    private val context: Context
) : RecyclerView.Adapter<SelectCourseItemAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_select_course_dialog, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item: ExerciseModel = exerciseModels[position]
        if (item.exerciseType.name == ("ListeningExercise"))
            holder.lblTitle.text = "Listening"
        else
            holder.lblTitle.text = item.exerciseType.name


        val layoutParams = holder.itemView.layoutParams
        val screenWidth: Int = getScreenWidth(holder.itemView.context)
        val margin: Int = dpToPx(holder.itemView.context, 8)
        var itemWidth: Float = (screenWidth / 2).toFloat()
        itemWidth -= (margin * 2)
        if (layoutParams is FlexboxLayoutManager.LayoutParams) {
            val marginLayoutParams = layoutParams
            marginLayoutParams.setMargins(0, (margin * 2), 0, 0);
            val flexboxParams = layoutParams as FlexboxLayoutManager.LayoutParams
            flexboxParams.flexBasisPercent = itemWidth / screenWidth
            holder.itemView.layoutParams = flexboxParams
            holder.itemView.setLayoutParams(marginLayoutParams);
        }
        holder.itemView.setOnClickListener {
            viewModel.loadExercise(item.id)
        }

    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    private fun getScreenWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels
    }

    override fun getItemCount(): Int = exerciseModels.size

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgExercise: ImageView = itemView.findViewById(R.id.imgExercise)
        val lblTitle: TextView = itemView.findViewById(R.id.lblTitle)
    }
}