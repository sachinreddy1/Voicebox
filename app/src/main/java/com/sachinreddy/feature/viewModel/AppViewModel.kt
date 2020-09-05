package com.sachinreddy.feature.viewModel

import androidx.lifecycle.ViewModel
import com.sachinreddy.feature.data.Track
import com.sachinreddy.feature.data.table.RowHeader
import com.sachinreddy.feature.data.table.TimelineHeader
import javax.inject.Inject

class AppViewModel @Inject constructor() : ViewModel() {
    var numberBars: Int = 8
    var mTrackList: MutableList<Track> = mutableListOf()
    var timelineHeaderList: MutableList<TimelineHeader>? = mutableListOf()

    init {
        val track = Track(RowHeader(""), numberBars, 0)
        // Setting the first cell to selected
        track.cellList?.let {
            it.first().isSelected = true
        }
        mTrackList.add(track)
        
        for (i in 0 until numberBars) {
            timelineHeaderList?.add(TimelineHeader(i + 1))
        }
    }
}