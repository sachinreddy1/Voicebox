package com.evrencoskun.tableview.data

class Cell(
    var isSelected: Boolean = false,
    var isPlaying: Boolean = false,
    var columnPosition: Int,
    var rowPosition: Int,
    var data: MutableList<ShortArray> = mutableListOf(),
    var bpm: Int = 120
) {
    fun isEqual(cell: Cell): Boolean {
        return (this.isSelected == cell.isSelected) &&
                (this.isPlaying == cell.isPlaying) &&
                (this.columnPosition == cell.columnPosition) &&
                (this.rowPosition == cell.rowPosition) &&
                (this.data.size == cell.data.size)
    }

    fun copy(): Cell {
        return Cell(
            isSelected = this.isSelected,
            isPlaying = this.isPlaying,
            columnPosition = this.columnPosition,
            rowPosition = this.rowPosition,
            data = this.data.toMutableList(),
            bpm = this.bpm
        )
    }
}