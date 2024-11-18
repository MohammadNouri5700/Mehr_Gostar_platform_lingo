//package com.android.platform.ui.exercises.ai_voice.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.android.platform.R
//import com.android.platform.ui.exercises.ai_context.AIContextViewModel
//import com.android.platform.utils.extension.animateLeft
//import com.android.platform.utils.extension.animateRight
//
//class AiVoiceAdapter(private var items: List<AiContextEntity>, val viewModel: AIContextViewModel, val isUs:Boolean, private val ctx: Context) : RecyclerView.Adapter<AiVoiceAdapter.TaskViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
//        val layout = if (viewType == 1) {
//            R.layout.item_ai_context_exercise_them
//        } else {
//            R.layout.item_ai_context_exercise_us
//        }
//        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
//        return TaskViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
//        holder.message.text = items[position].Sentence
//
//        if (getItemViewType(position)==0){
//                animateLeft(holder.message)
//        }else{
//                animateRight(holder.message)
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (position%2==0) 0 else 1
//    }
//
//    fun updateList(newItems: List<AiContextEntity>) {
//        val diffResult = DiffUtil.calculateDiff(AiContextDiffCallback(items, newItems))
//        items = newItems
//        diffResult.dispatchUpdatesTo(this)
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val message: TextView = itemView.findViewById(R.id.lblMessage)
//    }
//}