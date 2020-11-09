package com.sachinreddy.feature.data.table

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.sachinreddy.feature.R

class Cell(
    var isSelected: Boolean = false,
    var isPlaying: Boolean = false,
    var columnPosition: Int,
    var rowPosition: Int,
    var data: MutableList<ShortArray> = mutableListOf(),
    var track: AudioTrack? = null,
    var playerThread: Thread? = null,
    var cellButton: ImageButton? = null,
    var view: ConstraintLayout? = null
) {
    init {
        track = initPlayer()
    }

    /*
    [initPlayer] Get a new track when initialized.
     */
    private fun initPlayer(): AudioTrack {
        val maxJitter = AudioTrack.getMinBufferSize(
            8000,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        return AudioTrack(
            AudioManager.MODE_IN_COMMUNICATION,
            8000,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT,
            maxJitter,
            AudioTrack.MODE_STREAM
        )
    }

    /*
    [playTrack] Starts the track with the recorded data.
     */
    fun playTrack() {
        playerThread = object : Thread() {
            override fun run() {
                while (isPlaying) {
                    data.forEach {
                        track?.write(it, 0, 1024)
                    }
                }
            }
        }
        isPlaying = true
        cellButton?.setImageResource(R.drawable.ic_stop)
        playerThread?.start()
        track?.play()
    }

    /*
    [stopTrack] Stops the track that is currently being played.
     */
    fun stopTrack() {
        isPlaying = false
        cellButton?.setImageResource(R.drawable.ic_play)
        track?.pause()
        playerThread?.join()
    }
}