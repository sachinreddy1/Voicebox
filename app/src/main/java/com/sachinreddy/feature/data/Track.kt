package com.sachinreddy.feature.data

import com.google.firebase.database.IgnoreExtraProperties
import com.sachinreddy.feature.table.Cell
import com.sachinreddy.feature.table.ColumnHeader
import com.sachinreddy.feature.table.RowHeader

@IgnoreExtraProperties
class Track {
    var rowHeader: RowHeader = RowHeader("")
    var columnHeaderList: MutableList<ColumnHeader>? = mutableListOf()
    var cellList: MutableList<Cell>? = mutableListOf()

    var numberBars: Int = 8

    constructor() {
        for(i in 0..numberBars) {
            columnHeaderList?.add(ColumnHeader(i + 1))
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