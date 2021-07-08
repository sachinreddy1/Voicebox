package com.evrencoskun.tableview.data

import android.media.AudioFormat
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

//        val test = AudioTrack(
//            AudioManager.MODE_IN_COMMUNICATION, //
//            8000, //
//            AudioFormat.CHANNEL_OUT_STEREO,
//            AudioFormat.ENCODING_PCM_16BIT, //
//            minBuffSize, //
//            AudioTrack.MODE_STREAM
//        )

        return AudioTrack.Builder()
//            .setAudioAttributes(
//                AudioAttributes.Builder()
////                    .setLegacyStreamType(AudioManager.MODE_IN_COMMUNICATION)
////                    .setUsage(AudioAttributes.USAGE_MEDIA)
////                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build()
//            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(8000)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                    .build()
            )
            .setTransferMode(AudioTrack.MODE_STREAM)
            .setBufferSizeInBytes(minBuffSize)
            .setOffloadedPlayback(true)
            .build()
    }
}