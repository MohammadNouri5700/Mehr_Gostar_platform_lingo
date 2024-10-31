package com.android.platform.ui.home.story

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.R
import com.android.platform.StorisReply
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.delay

class StoryAdapter(private val items: StorisReply) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.txtTime)
        val imageView: ImageView = view.findViewById(R.id.imgProfile)
        val shimmer: ShimmerFrameLayout = view.findViewById(R.id.shiStory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_story_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items.getStoris(position).fullName
            holder.shimmer.hideShimmer()
        holder.imageView.setOnClickListener {
                holder.shimmer.hideShimmer()
        }
    }

    override fun getItemCount() = items.storisCount
}
