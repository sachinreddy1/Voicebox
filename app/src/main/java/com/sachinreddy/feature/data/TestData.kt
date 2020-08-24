package com.sachinreddy.feature.data

import com.sachinreddy.feature.table.Cell
import com.sachinreddy.feature.table.TimelineHeader
import com.sachinreddy.feature.table.RowHeader

object TestData {
    val friends = mutableListOf(
        "0P1exfUcj0NiQYuEq2sdiru1ibn1",
        "3jEGJpbdXmVLqJ2nwlDDvGgO4Q32",
        "qm79GNxfACh6z5om7kz85Ea6ZYJ2"
    )

    val songs_: List<Song> = listOf(
        Song(
            "3jEGJpbdXmVLqJ2nwlDDvGgO4Q32",
            "Untitled",
            false
        ),
        Song(
            "0P1exfUcj0NiQYuEq2sdiru1ibn1",
            "Miss u bitch",
            false
        )
    )

    val mRowHeaderList: List<RowHeader>? = listOf(
        RowHeader("Vocal"),
        RowHeader("Piano"),
        RowHeader("Drum Kit")
    )

    val M_TIMELINE_HEADER_LIST: List<TimelineHeader>? = listOf(
        TimelineHeader("1"),
        TimelineHeader("2"),
        TimelineHeader("3"),
        TimelineHeader("4"),
        TimelineHeader("5"),
        TimelineHeader("6"),
        TimelineHeader("7"),
        TimelineHeader("8")
    )

    val mCellList: List<List<Cell>>? = listOf(
        listOf(
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello")
        ),
        listOf(
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello")
        ),
        listOf(
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello"),
            Cell("Hello")
        )
    )
}
