package com.sachinreddy.feature.table.data

data class RecordBuffer(
    var data: MutableList<ShortArray> = mutableListOf(),
    val bpm: Int = 120
)