package com.android.platform.di.factory


import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.android.platform.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.TimeBar


class V1ExoPlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private var isFullscreen = false
    private val playerView: PlayerView
    private var playPauseButton: ImageButton
    private val fullscreenButton: ImageButton

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
        setupProgressBar()
        setupControls()
        setupFullscreenButton()
    }

    private fun setupFullscreenButton() {
        fullscreenButton.setOnClickListener {
            toggleFullscreen()
        }
    }

    private fun toggleFullscreen() {
        if (isFullscreen) {
            exitFullscreen()
        } else {
            enterFullscreen()
        }
        isFullscreen = !isFullscreen
    }

    private fun enterFullscreen() {

//        fullscreenDialog.show()
    }

    private fun exitFullscreen() {
        fullscreenButton.setImageResource(R.drawable.play_green)

        val window = (context as Activity).window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE


        (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        radius = initialRadius
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        requestLayout()
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
                    setImageResource(R.drawable.play_green)
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


    fun setPlayer(exoPlayer: ExoPlayer) {
        playerView.player = exoPlayer


        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                updateProgress()
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
        handler?.removeCallbacksAndMessages(null)
    }
}