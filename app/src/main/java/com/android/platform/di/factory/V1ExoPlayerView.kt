package com.android.platform.di.factory


import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.android.platform.R
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory


class V1ExoPlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.e("APP", "onDetachedFromWindow")
        player.release()
    }

    private val playerView: PlayerView
    private var playPauseButton: ImageButton
    private val fullscreenButton: ImageButton
    private lateinit var player: ExoPlayer

    //    private var rewindButton: ImageButton
//    private var forwardButton: ImageButton
    private var progressBar: DefaultTimeBar

    //    private var currentTimeTextView: TextView
//    private var totalTimeTextView: TextView
    private val initialRadius = resources.getDimension(com.intuit.sdp.R.dimen._25sdp)

    init {
        radius = initialRadius
        elevation = resources.getDimension(com.intuit.sdp.R.dimen._1sdp)

        LayoutInflater.from(context).inflate(R.layout.v2_exo_player, this, true)

        playerView = findViewById(R.id.player_view)
        playPauseButton = findViewById(R.id.play_pause_button)
        fullscreenButton = findViewById(R.id.fullscreen_button)

//        rewindButton = findViewById(R.id.rewind_button)
//        forwardButton = findViewById(R.id.forward_button)
        progressBar = findViewById(R.id.progress_bar)
//        currentTimeTextView = findViewById(R.id.current_time)
//        totalTimeTextView = findViewById(R.id.total_time)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)  // استفاده برای پخش رسانه
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)  // نوع محتوا
            .build()


        setupProgressBar()
        setupControls()
        setupFullscreenButton()
        initVideo(context)
    }

    private fun setupFullscreenButton() {
        fullscreenButton.setOnClickListener {
            toggleFullscreen()
        }
    }

    private fun initVideo(context: Context) {
        // ساخت RenderersFactory
        val renderersFactory = DefaultRenderersFactory(context)
            .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)

        // انتخاب TrackSelector
        val trackSelector = DefaultTrackSelector(context)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)  // استفاده برای پخش رسانه
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)  // نوع محتوا

            .build()

        // ساخت ExoPlayer
        player = ExoPlayer.Builder(context)
            .setRenderersFactory(renderersFactory)
            .setTrackSelector(trackSelector)
            .setAudioAttributes(audioAttributes, true)
            .build()

        // تنظیم ویژگی‌های صدا


        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.mode = AudioManager.MODE_NORMAL

        val audioSession = player.audioSessionId
        Log.d("ExoPlayer", "Audio Session ID: $audioSession")


        playerView.player = player

        setPlayer(player)
    }

    private fun toggleFullscreen() {
        enterFullscreen()
    }

    private fun enterFullscreen() {
        val activity = context as? AppCompatActivity ?: return
        val videoUri = player.currentMediaItem?.localConfiguration?.uri ?: return
        val fullscreenDialog =
            FullscreenVideoDialogFragment.newInstance(videoUri, player.currentPosition)
        fullscreenDialog.show(activity.supportFragmentManager, "FullscreenVideoDialog")
        playerPause()
    }


    private fun setupProgressBar() {
        progressBar.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {

            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
//                currentTimeTextView.text = formatTime(position)
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                val player = playerView.player ?: return
                if (!canceled) {
                    player.seekTo(position)
                    updateProgress()
                }
            }
        })
    }

    private fun playerPause() {
        if (player.isPlaying) {
            player.pause()
            playPauseButton.apply {
                setImageResource(R.drawable.play_green)
                background = null
                imageTintList = null
            }
        }
    }

    private fun playerReset() {
            player.seekTo(0)
            player.pause()
            playPauseButton.apply {
                setImageResource(R.drawable.play_green)
                background = null
                imageTintList = null
            }
    }

    private fun togglePlay() {
        if (player.isPlaying) {
            player.pause()
            playPauseButton.apply {
                setImageResource(R.drawable.play_green)
                background = null
                imageTintList = null
            }
        } else {
            player.play()
            playPauseButton.apply {
                setImageResource(R.drawable.pause)
                background = null
                imageTintList = null
            }
        }
    }

    private fun setupControls() {
        playPauseButton.setOnClickListener {
            val player = playerView.player ?: return@setOnClickListener
            if (player.isPlaying) {
                player.pause()
                playPauseButton.apply {
                    setImageResource(R.drawable.play_green)
                    background = null
                    imageTintList = null
                }
            } else {
                player.play()
                playPauseButton.apply {
                    setImageResource(R.drawable.pause)
                    background = null
                    imageTintList = null
                }
            }
        }

//        rewindButton.setOnClickListener {
//            playerView.player?.seekBack()
//        }

//        forwardButton.setOnClickListener {
//            playerView.player?.seekForward()
//        }

//        playerView.player?.addListener(object : Player.Listener {
//            override fun onPlaybackStateChanged(state: Int) {
//                val duration = playerView.player?.duration ?: 0
//                val position = playerView.player?.currentPosition ?: 0
////                currentTimeTextView.text = formatTime(position)
////                totalTimeTextView.text = formatTime(duration)
//                progressBar.setPosition(position)
//                progressBar.setDuration(duration)
//            }
//        })
    }

    private fun formatTime(ms: Long): String {
        val seconds = ms / 1000 % 60
        val minutes = ms / (1000 * 60) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun setSource(value: String) {
        val mediaSource = ProgressiveMediaSource.Factory(DefaultDataSourceFactory(context))
            .createMediaSource(MediaItem.fromUri("https://dl.lingomars.ir/general/video.mp4"))


        player.setMediaSource(mediaSource)
        player.prepare()
    }

    private fun setPlayer(exoPlayer: ExoPlayer) {
        playerView.player = exoPlayer


        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                updateProgress()
            }

            override fun onPlayerError(error: PlaybackException) {
                Log.e("ExoPlayer", "Player error: ${error.message}")
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) startProgressUpdater() else stopProgressUpdater()
            }
        })
    }

    private fun updateProgress() {
        val player = playerView.player ?: return
        val currentPosition = player.currentPosition
        val duration = player.duration

        progressBar.setDuration(duration)
        progressBar.setPosition(currentPosition)

//        currentTimeTextView.text = formatTime(currentPosition)
//        totalTimeTextView.text = formatTime(duration)
    }

    private fun startProgressUpdater() {

        handler?.post(object : Runnable {
            override fun run() {
                updateProgress()
                handler?.postDelayed(this, 1000)
            }
        })
    }


    private fun stopProgressUpdater() {
        playerReset()
        handler?.removeCallbacksAndMessages(null)
    }
}