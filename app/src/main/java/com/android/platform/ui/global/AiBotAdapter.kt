package com.android.platform.ui.global

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.R
import com.android.platform.repository.data.model.BotMessageEntity
import com.android.platform.repository.data.model.MType
import com.android.platform.ui.exercises.ai_context.AIContextViewModel
import com.android.platform.utils.extension.animateLeft
import com.android.platform.utils.extension.animateRight

class AiBotAdapter(
    private var items: List<BotMessageEntity>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (items[viewType].type == MType.messageText) { // Message type
            val layout = if (viewType % 2 == 0) {
                R.layout.item_ai_bot_them
            } else {
                R.layout.item_ai_bot_message_us
            }
            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            MessageViewHolder(view)
        } else { // Voice type
            val layout = R.layout.item_ai_bot_voice_us
            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            VoiceViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is MessageViewHolder) {
            holder.message.text = item.message

            if (getItemViewType(position) == 0) {
                animateLeft(holder.message)
            } else {
                animateRight(holder.message)
            }
        } else if (holder is VoiceViewHolder) {
            // Handle voice type binding logic if needed
            // For example, show playback options or voice animations
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateList(newItems: List<BotMessageEntity>) {
        val diffResult = DiffUtil.calculateDiff(AiBotDiffCallback(items, newItems))
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = items.size

    // ViewHolder for messages
    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.lblMessage)
    }

    // ViewHolder for voice messages
    class VoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define other views specific to the voice type here, if necessary
    }
}
