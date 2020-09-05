package com.sachinreddy.feature.data

import com.google.firebase.database.IgnoreExtraProperties
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.data.table.TimelineHeader
import com.sachinreddy.feature.data.table.RowHeader

@IgnoreExtraProperties
class Track {
    var rowHeader: RowHeader = RowHeader("")
    var cellList: MutableList<Cell>? = mutableListOf()

    constructor(
        rowHeader: RowHeader,
        numberBars: Int,
        rowPosition: Int
    ) {
        this.rowHeader = rowHeader

        for(i in 0 until numberBars) {
            val cell = Cell(
                data = rowPosition.toString(),
                columnPosition = i,
                rowPosition = rowPosition,
                isSelected = false,
                hasData = false
            )
            cellList?.add(cell)
        }
    }
}