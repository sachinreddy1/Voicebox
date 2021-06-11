package com.evrencoskun.tableview.data

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack

class Track(
    var cells: List<Cell>,
    var audioTrack: AudioTrack? = null,
    var playerThread: Thread? = null,
    var data: MutableMap<Int, ShortArray> = mutableMapOf(),
    var isPlaying: Boolean = false
) {
    init {
        audioTrack = initPlayer()
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
}