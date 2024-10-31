package com.android.platform.ui.home.story

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.PodcastCategoryReply
import com.android.platform.R
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.delay

class PodcastAdapter(val items: PodcastCategoryReply) :
    RecyclerView.Adapter<PodcastAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.txttitle)
//        val imageView: ImageView = view.findViewById(R.id.imgProfile)
//        val shimmer: ShimmerFrameLayout = view.findViewById(R.id.shiStory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_podcasts_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items.getPodcastCategories(position).name
//        if (position!=0)
//            holder.shimmer.hideShimmer()
//        holder.imageView.setOnClickListener {
//                holder.shimmer.hideShimmer()
//        }
    }

    override fun getItemCount() = items.podcastCategoriesCount
}
