package com.sachinreddy.feature.table.data

data class RecordBuffer(
    var data: MutableList<ShortArray> = mutableListOf(),
    var bpm: Int = 120,
    var lastRecordedPosition: Int = 0
)