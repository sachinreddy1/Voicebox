package com.sachinreddy.feature.data.table

class RowHeader(
    val rowPosition: Int = 0
) {
    fun isEqual(rowHeader: RowHeader): Boolean {
        return this.rowPosition == rowHeader.rowPosition
    }
}