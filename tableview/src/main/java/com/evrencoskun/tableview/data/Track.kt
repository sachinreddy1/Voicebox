package com.evrencoskun.tableview.data

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.M)
class Track(
    var cells: List<Cell>,
    var audioTrack: AudioTrack? = null,
    var playerThread: Thread? = null
) {
    init {
        audioTrack = initPlayer()
    }

    /*
    [initPlayer] Get a new track when initialized.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initPlayer(): AudioTrack {
        val minBuffSize = AudioTrack.getMinBufferSize(
            8000,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        return AudioTrack(
            AudioManager.MODE_IN_COMMUNICATION,
            8000,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT,
            minBuffSize,
            AudioTrack.MODE_STREAM
        )
    }
}