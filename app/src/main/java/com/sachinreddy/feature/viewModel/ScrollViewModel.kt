package com.sachinreddy.feature.viewModel

import android.media.AudioManager
import android.media.AudioRecord
import androidx.lifecycle.ViewModel
import com.evrencoskun.tableview.TableView
import com.sachinreddy.feature.data.Track
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.data.table.TimelineHeader
import javax.inject.Inject

class ScrollViewModel @Inject constructor() : ViewModel() {

    var numberBars: Int = 8

    var mTrackList: MutableList<Track> = mutableListOf()
    var timelineHeaderList: MutableList<TimelineHeader>? = mutableListOf()

    var selectedCell: Cell? = null
    var draggedCell: Cell? = null

    var isRecording = false

    var audioManager: AudioManager? = null
    var recorder: AudioRecord? = null

    var tableView: TableView? = null
}