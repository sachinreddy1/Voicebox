package com.sachinreddy.feature.data.table

import android.media.AudioTrack
import android.widget.ImageButton
import androidx.lifecycle.MutableLiveData

class Cell(
    var isSelected: Boolean = false,
    var isPlaying: Boolean = false,

    var columnPosition: Int,
    var rowPosition: Int? = null,

    var data: MutableList<ShortArray> = mutableListOf(),

    var track: AudioTrack? = null,
    var playerThread: Thread? = null,

    var cellButton: ImageButton? = null
)