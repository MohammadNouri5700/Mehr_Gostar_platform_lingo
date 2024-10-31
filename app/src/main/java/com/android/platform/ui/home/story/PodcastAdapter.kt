package com.android.platform.ui.home.story

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.PodcastCategory
import com.android.platform.R
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.repository.data.database.ImageDao
import com.android.platform.utils.extension.byteArrayToBitmap
import com.android.platform.utils.extension.saveImageFromUrl
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class PodcastAdapter(val items: MutableList<PodcastCategory>,
                     val imageDao: ImageDao,
                     val call: CallQueueManager,
                    val context: Context) :
    RecyclerView.Adapter<PodcastAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.txttitle)
        val imgcompat: ImageView = view.findViewById(R.id.imgcompat)
//        val shimmer: ShimmerFrameLayout = view.findViewById(R.id.shiStory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_podcasts_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        call.enqueueIoTask {
            val podcastCategory = items[position]
            holder.textView.text = podcastCategory.name
            val test = imageDao.getImageByUrl(podcastCategory.icon)
            if (test==null){
                call.enqueueMainTask {
                    Glide.with(context).load(podcastCategory.icon).into(holder.imgcompat);
                }
                saveImageFromUrl(podcastCategory.icon,imageDao,context)
            }else{
                val data = byteArrayToBitmap(test.imageData)
                call.enqueueMainTask {
                    holder.imgcompat.setImageBitmap(data)
                }
            }


        }


//        if (position!=0)
//            holder.shimmer.hideShimmer()
//        holder.imageView.setOnClickListener {
//                holder.shimmer.hideShimmer()
//        }
    }

    override fun getItemCount() = items.size
}
