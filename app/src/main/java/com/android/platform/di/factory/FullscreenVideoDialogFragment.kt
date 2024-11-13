package com.android.platform.di.factory


import android.app.Dialog
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.android.platform.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView



class FullscreenVideoDialogFragment : DialogFragment() {

    private lateinit var player: ExoPlayer
    private lateinit var videoUri: Uri
    private var originalOrientation: Int = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    private var currentPosition: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        originalOrientation = requireActivity().requestedOrientation

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fullscreen_video, container, false)

        videoUri = arguments?.getParcelable("video_uri") ?: throw IllegalArgumentException("Video URI is missing")
        currentPosition = arguments?.getLong("current_position", 0L) ?: 0L
        player = ExoPlayer.Builder(requireContext()).build()
        player.setMediaItem(MediaItem.fromUri(videoUri))

        val playerView = view.findViewById<PlayerView>(R.id.player_view)
        playerView.player = player

        player.prepare()


        player.seekTo(currentPosition)
        player.play()

        val closeButton = view.findViewById<ImageButton>(R.id.closeimg)
        closeButton.setOnClickListener {
            currentPosition = player.currentPosition
            player.release()
            dismiss()
        }


        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
        requireActivity().requestedOrientation = originalOrientation
    }

    companion object {
        fun newInstance(videoUri: Uri, currentPosition: Long): FullscreenVideoDialogFragment {
            return FullscreenVideoDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("video_uri", videoUri)
                    putLong("current_position", currentPosition)
                }
            }
        }
    }
}
