package com.example.assignment2

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

/** Screen D: play one audio within app using MediaPlayer. */
class AudioFragment : Fragment(R.layout.fragment_audio) {

    private val audioUrls = listOf(
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
    )

    private var player: MediaPlayer? = null
    private lateinit var tvStatus: TextView
    private var currentUrlIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvStatus = view.findViewById(R.id.tvStatus)

        view.findViewById<Button>(R.id.btnPlay).setOnClickListener { play() }
        view.findViewById<Button>(R.id.btnPause).setOnClickListener {
            if (player?.isPlaying == true) {
                player?.pause()
                tvStatus.text = "Audio Paused"
            }
        }
        view.findViewById<Button>(R.id.btnStop).setOnClickListener {
            releasePlayer()
            tvStatus.text = "Audio Stopped"
        }
    }

    private fun play() {
        val existing = player
        if (existing != null) {
            existing.start()
            tvStatus.text = "Playing Audio"
            return
        }

        tvStatus.text = "Loading audio..."
        initAndPlay(audioUrls[currentUrlIndex])
    }

    private fun initAndPlay(url: String) {
        try {
            player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener { mp ->
                    mp.start()
                    tvStatus.text = "Playing Audio"
                }
                setOnCompletionListener {
                    releasePlayer()
                    tvStatus.text = "Audio Finished"
                }
                setOnErrorListener { _, _, _ ->
                    releasePlayer()
                    if (currentUrlIndex < audioUrls.size - 1) {
                        currentUrlIndex++
                        initAndPlay(audioUrls[currentUrlIndex])
                    } else {
                        tvStatus.text = "Failed to load audio stream."
                    }
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            tvStatus.text = "Error: ${e.message}"
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }

    override fun onDestroyView() {
        releasePlayer()
        super.onDestroyView()
    }
}
