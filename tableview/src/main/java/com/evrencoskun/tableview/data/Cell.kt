package com.evrencoskun.tableview.data

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack

class Cell(
    var isSelected: Boolean = false,
    var isPlaying: Boolean = false,
    var columnPosition: Int,
    var rowPosition: Int,
    var data: MutableList<ShortArray> = mutableListOf(),
    var track: AudioTrack? = null,
    var playerThread: Thread? = null
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

    fun isEqual(cell: Cell): Boolean {
        return (this.isSelected == cell.isSelected) && (this.isPlaying == cell.isPlaying) && (this.columnPosition == cell.columnPosition) && (this.rowPosition == cell.rowPosition) && (this.data == cell.data)
    }
}