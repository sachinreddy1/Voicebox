package com.evrencoskun.tableview.data

class Timeline(
    val columnPosition: Int = 0
) {
    fun isEqual(timeline: Timeline): Boolean {
        return this.columnPosition == timeline.columnPosition
    }
}