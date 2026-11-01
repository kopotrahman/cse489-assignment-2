package com.example.assignment2

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment

/** Screen C: play one video within the app using VideoView / MediaPlayer. */
class VideoFragment : Fragment(R.layout.fragment_video) {

    private var videoView: VideoView? = null
    private var progressBar: ProgressBar? = null
    private var tvVideoStatus: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vv = view.findViewById<VideoView>(R.id.videoView)
        videoView = vv
        progressBar = view.findViewById(R.id.progressBar)
        tvVideoStatus = view.findViewById(R.id.tvVideoStatus)

        val btnPlay = view.findViewById<Button>(R.id.btnPlayVideo)
        val btnPause = view.findViewById<Button>(R.id.btnPauseVideo)
        val btnRetry = view.findViewById<Button>(R.id.btnRetryVideo)

        val controller = MediaController(requireContext())
        controller.setAnchorView(vv)
        vv.setMediaController(controller)

        vv.setOnPreparedListener { mp ->
            progressBar?.visibility = View.GONE
            tvVideoStatus?.visibility = View.GONE
            mp.isLooping = true
            mp.start()
        }

        vv.setOnErrorListener { _, _, _ ->
            progressBar?.visibility = View.GONE
            tvVideoStatus?.visibility = View.VISIBLE
            tvVideoStatus?.text = "Error playing video. Tap Retry."
            true
        }

        btnPlay.setOnClickListener {
            if (vv.isPlaying.not()) {
                vv.start()
                tvVideoStatus?.visibility = View.GONE
            }
        }

        btnPause.setOnClickListener {
            if (vv.isPlaying) {
                vv.pause()
                tvVideoStatus?.visibility = View.VISIBLE
                tvVideoStatus?.text = "Video Paused"
            }
        }

        btnRetry.setOnClickListener {
            loadVideo()
        }

        loadVideo()
    }

    private fun loadVideo() {
        progressBar?.visibility = View.VISIBLE
        tvVideoStatus?.visibility = View.VISIBLE
        tvVideoStatus?.text = "Loading video..."

        val rawVideoUri = Uri.parse("android.resource://${requireContext().packageName}/${R.raw.sample_video}")
        videoView?.setVideoURI(rawVideoUri)
    }

    override fun onPause() {
        super.onPause()
        videoView?.pause()
    }

    override fun onDestroyView() {
        videoView?.stopPlayback()
        videoView = null
        progressBar = null
        tvVideoStatus = null
        super.onDestroyView()
    }
}
