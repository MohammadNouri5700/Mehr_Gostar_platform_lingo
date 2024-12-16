package com.android.platform.ui.global

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.R
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.repository.data.model.BotMessageEntity
import com.android.platform.ui.exercises.ai_context.adapter.MType
import com.android.platform.ui.global.AiBotAdapter.VoiceViewHolder
import com.android.platform.utils.extension.animateLeft
import com.android.platform.utils.extension.animateRight
import com.masoudss.lib.SeekBarOnProgressChanged
import com.masoudss.lib.WaveformSeekBar
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AiBotAdapter(
    private var items: List<BotMessageEntity>,
    private val context: Context,
    private val call: CallQueueManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (items[viewType].type == MType.Text) { // Message type
            val layout = if (items[viewType].sideUs) {
                R.layout.item_ai_bot_message_us
            } else {
                R.layout.item_ai_bot_message_them
            }
            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            MessageViewHolder(view)
        } else { // Voice type

            val layout = if (items[viewType].sideUs) {
                R.layout.item_ai_bot_voice_us
            } else {
                R.layout.item_ai_bot_voice_them
            }

            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            VoiceViewHolder(view)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is MessageViewHolder) {
            holder.message.text = item.message


            if (item.sideUs) {
                animateLeft(holder.itemView)
            } else {
                animateRight(holder.itemView)
            }

            if (item.fade) {
                holder.itemView.alpha = 0.7f
            } else {
                holder.itemView.alpha = 1f
            }


        } else if (holder is VoiceViewHolder) {

            if (item.sideUs) {
                animateLeft(holder.itemView)
                holder.isDownloaded = true
                holder.progress.visibility = View.GONE
                processAudio(holder)
            } else {
                animateRight(holder.itemView)
                call.enqueueIoTask {
                    downloadAudioFile(
                        item.message,
                        "https://dl.lingomars.ir/general/sway.mp3",
                        holder
                    )
                }
            }

            holder.imgPlay.setOnClickListener {
                if (!holder.isPlaying) {
                    playVoice(getFile(item.message), holder)
                } else {
                    pauseVoice(holder)
                }

            }
        }
    }


    private fun downloadAudioFile(fileName: String, url: String, holder: VoiceViewHolder) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        call.enqueueIoTask {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val contentLength = response.body?.contentLength() ?: 0
                    val inputStream = response.body?.byteStream()
                    var audioFile = File(context.cacheDir, fileName)
                    val outputStream = FileOutputStream(audioFile)

                    val buffer = ByteArray(8 * 1024)
                    var totalRead = 0L
                    var read: Int
                    var tmp = 0
                    while (inputStream?.read(buffer).also { read = it ?: -1 } != -1) {
                        outputStream.write(buffer, 0, read)
                        totalRead += read
                        val percent = (totalRead * 100 / contentLength).toInt()

                        if (percent > 15 && percent - tmp > 2) {
                            tmp = percent
                            if (holder.progress.isIndeterminate) {
                                call.enqueueMainTask {
                                    holder.progress.isIndeterminate = false
                                }
                            }
                            call.enqueueMainTask {
                                holder.progress.setProgress(percent, true)
                            }
                        }

                    }
                    call.enqueueMainTask {
                        holder.progress.visibility = View.GONE
                    }

                    inputStream?.close()
                    outputStream.close()
                    processAudio(holder)
                    holder.isDownloaded = true
                }
            } catch (e: Exception) {
                Log.e("DownloadError", e.message.toString())
            }
        }
    }

    private fun processAudio(holder: VoiceViewHolder) {
        val fakeWaveform = IntArray(100) { (0..100).random() }
        holder.wave.sample = fakeWaveform
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateList(newItems: List<BotMessageEntity>) {
        val diffResult = DiffUtil.calculateDiff(AiBotDiffCallback(items, newItems))
        items = newItems
        Handler(Looper.getMainLooper()).post {
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun getItemCount(): Int = items.size

    // ViewHolder for messages
    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.lblMessage)
    }

    // ViewHolder for voice messages
    class VoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wave: WaveformSeekBar = itemView.findViewById(R.id.wave)
        val imgPlay: ImageView = itemView.findViewById(R.id.imgPlay)
        var media: MediaPlayer? = null
        var isPlaying: Boolean = false
        var isDownloaded: Boolean = false
        var progress: ProgressBar = itemView.findViewById(R.id.prDownload)


    }

    private fun pauseVoice(holder: VoiceViewHolder) {
        holder.isPlaying = false
        holder.media?.pause()
        call.enqueueMainTask {
            holder.imgPlay.apply {
                alpha = 0f;
                setImageResource(R.drawable.play_white)
            }.animate().alpha(1f).setDuration(300).start()
        }
    }

    private fun playVoice(file: File, holder: VoiceViewHolder) {
        if (!file.exists() || !holder.isDownloaded) {
            return
        }

        try {

            holder.isPlaying = true
            Log.e("APP", "FILE LOCATION IS = ${file.absolutePath}")



            if (holder.media == null) {
                holder.media = MediaPlayer()

                holder.media?.setDataSource(file.path)

                holder.media?.setOnPreparedListener {
                    holder.wave.maxProgress = it.duration.toFloat()
                    holder.media?.start()
                    updateSeekBar(holder)
                }
                holder.wave.onProgressChanged = object : SeekBarOnProgressChanged {
                    override fun onProgressChanged(
                        waveformSeekBar: WaveformSeekBar,
                        progress: Float,
                        fromUser: Boolean
                    ) {
                        if (fromUser) {
                            holder.media?.seekTo(progress.toInt())
                        }

                    }
                }
                holder.media?.prepareAsync()
            } else {
                holder.media?.start()
                updateSeekBar(holder)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        call.enqueueMainTask {
            holder.imgPlay.apply {
                alpha = 0f;
                setImageResource(R.drawable.pause_white)
            }.animate().alpha(1f).setDuration(300).start()
        }


    }

    private fun resetPlayer(holder: VoiceViewHolder) {
        holder.media?.seekTo(0)
        holder.wave.progress = 0f
        holder.media?.pause()
        call.enqueueMainTask {
            holder.imgPlay.apply {
                alpha = 0f;
                setImageResource(R.drawable.play_white)
            }.animate().alpha(1f).setDuration(300).start()
        }
        holder.isPlaying = false
    }

    private fun updateSeekBar(holder: VoiceViewHolder) {
        call.enqueueMainTask {
            while (holder.media!!.isPlaying) {
                holder.wave.progress = holder.media!!.currentPosition.toFloat()
                kotlinx.coroutines.delay(1)
            }
            if (990.0f <= (holder.wave.progress * 1000 / holder.wave.maxProgress)) {
                resetPlayer(holder)
            }
        }
    }

    private fun getFile(fileName: String): File {
        return File(context.cacheDir, fileName)
    }


}
