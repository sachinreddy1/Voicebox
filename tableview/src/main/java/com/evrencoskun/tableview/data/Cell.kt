package com.evrencoskun.tableview.data

class Cell(
    var isSelected: Boolean = false,
    var columnPosition: Int,
    var rowPosition: Int,
    var data: MutableList<ShortArray> = mutableListOf(),
    var bpm: Int = 120,
    var lastRecordedPosition: Int = 0
) {
    fun isEqual(cell: Cell): Boolean {
        return (this.isSelected == cell.isSelected) &&
                (this.columnPosition == cell.columnPosition) &&
                (this.rowPosition == cell.rowPosition) &&
                (this.data.size == cell.data.size) &&
                (this.bpm == cell.bpm) &&
                (this.lastRecordedPosition == cell.lastRecordedPosition)
    }

    fun copy(): Cell {
        return Cell(
            isSelected = this.isSelected,
            columnPosition = this.columnPosition,
            rowPosition = this.rowPosition,
            data = this.data.toMutableList(),
            bpm = this.bpm,
            lastRecordedPosition = this.lastRecordedPosition
        )
    }
}