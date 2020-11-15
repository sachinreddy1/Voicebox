package com.sachinreddy.feature.viewModel

import android.media.AudioManager
import android.media.AudioRecord
import androidx.lifecycle.ViewModel
import com.sachinreddy.feature.data.Track
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.data.table.RowHeader
import com.sachinreddy.feature.data.table.TimelineHeader
import javax.inject.Inject

class AppViewModel @Inject constructor() : ViewModel() {

    var numberBars: Int = 8

    var mTrackList: MutableList<Track> = mutableListOf()
    var timelineHeaderList: MutableList<TimelineHeader>? = mutableListOf()

    var startingCell: Cell? = null
    var selectedCells: MutableList<Cell> = mutableListOf()
    var draggedCell: Cell? = null

    var audioManager: AudioManager? = null
    var recorder: AudioRecord? = null

    var isRecording = false
    var isSelecting: Boolean = false

    init {
        // Add first track
        val track = Track(RowHeader(""), numberBars, 0)
        mTrackList.add(track)

        // Add timelineHeaders
        for (i in 0 until numberBars) {
            timelineHeaderList?.add(TimelineHeader(i + 1))
        }

        // Select the first cell
        mTrackList.first().cellList?.first()?.let {
            it.isSelected = true
            selectedCells.clear()
            selectedCells.add(it)
        }
    }
}