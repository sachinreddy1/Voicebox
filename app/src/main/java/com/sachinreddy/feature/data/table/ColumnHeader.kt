package com.sachinreddy.feature.data.table

class ColumnHeader(
    val columnPosition: Int = 0
) {
    fun isEqual(columnHeader: ColumnHeader): Boolean {
        return this.columnPosition == columnHeader.columnPosition
    }
}