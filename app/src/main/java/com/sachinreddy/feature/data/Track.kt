package com.sachinreddy.feature.data

import com.google.firebase.database.IgnoreExtraProperties
import com.sachinreddy.feature.table.Cell
import com.sachinreddy.feature.table.TimelineHeader
import com.sachinreddy.feature.table.RowHeader

@IgnoreExtraProperties
class Track {
    var rowHeader: RowHeader = RowHeader("")
    var timelineHeaderList: MutableList<TimelineHeader>? = mutableListOf()
    var cellList: MutableList<Cell>? = mutableListOf()

    var numberBars: Int = 8

    constructor() {
        for(i in 0..numberBars) {
            timelineHeaderList?.add(TimelineHeader(i + 1))
            cellList?.add(Cell("test"))
        }
    }

    constructor(
        rowHeader: RowHeader,
        numberBars: Int
    ) {
        this.rowHeader = rowHeader
        this.numberBars = numberBars
    }
}