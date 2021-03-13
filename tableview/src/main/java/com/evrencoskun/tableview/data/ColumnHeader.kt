package com.evrencoskun.tableview.data

class ColumnHeader(
    val columnPosition: Int = 0
) {
    fun isEqual(columnHeader: ColumnHeader): Boolean {
        return this.columnPosition == columnHeader.columnPosition
    }
}