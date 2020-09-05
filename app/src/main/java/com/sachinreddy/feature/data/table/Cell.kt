package com.sachinreddy.feature.data.table

class Cell(
    val data: Any,
    var isSelected: Boolean = false,
    var columnPosition: Int,
    var rowPosition: Int? = null
)