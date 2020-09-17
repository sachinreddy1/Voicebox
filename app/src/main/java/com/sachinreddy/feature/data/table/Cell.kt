package com.sachinreddy.feature.data.table

import java.nio.ByteBuffer
import java.util.*

class Cell(
    var isSelected: Boolean = false,
    var isPlaying: Boolean = false,

    var columnPosition: Int,
    var rowPosition: Int? = null,
    var hasData: Boolean = false,

    var data: ShortArray? = ShortArray(1024)
)