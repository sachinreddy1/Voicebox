package com.sachinreddy.feature.data.table

import androidx.lifecycle.MutableLiveData

class Cell(
    var isSelected: Boolean = false,
    var isPlaying: Boolean = false,

    var columnPosition: Int,
    var rowPosition: Int? = null,
    var hasData: Boolean = false,

    var data: MutableList<ShortArray> = mutableListOf()
)