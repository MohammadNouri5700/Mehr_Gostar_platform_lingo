package com.android.platform.ui.home.story

import android.content.Context
import android.graphics.drawable.Drawable
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
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.repository.data.database.ImageDao
import com.android.platform.utils.extension.byteArrayToBitmap
import com.android.platform.utils.extension.saveImageFromUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.delay

class StoryAdapter(private val items: StorisReply, val imageDao: ImageDao,
                   val call: CallQueueManager,
                   val context: Context
) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.txtTime)
        val txtLocation: TextView = view.findViewById(R.id.txtLocation)
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
        holder.txtLocation.text = items.getStoris(position).location
        call.enqueueIoTask {
            val test = imageDao.getImageByUrl(items.getStoris(position).picture)
            if (test==null){
                call.enqueueMainTask {
                    Glide.with(context).load(items.getStoris(position).picture).listener(object :
                        RequestListener<Drawable> {

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            holder.shimmer.hideShimmer()
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    }).into(holder.imageView);
                }
                saveImageFromUrl(items.getStoris(position).picture,imageDao,context)
            }else{
                val data = byteArrayToBitmap(test.imageData)
                call.enqueueMainTask {
                    holder.imageView.setImageBitmap(data)
                    holder.shimmer.hideShimmer()
                }
            }
        }

    }

    override fun getItemCount() = items.storisCount
}
