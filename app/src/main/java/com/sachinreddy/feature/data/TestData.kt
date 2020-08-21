package com.sachinreddy.feature.data

import com.sachinreddy.feature.table.Cell
import com.sachinreddy.feature.table.ColumnHeader
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

    val mColumnHeaderList: List<ColumnHeader>? = listOf(
        ColumnHeader("1"),
        ColumnHeader("2"),
        ColumnHeader("3"),
        ColumnHeader("4"),
        ColumnHeader("5"),
        ColumnHeader("6"),
        ColumnHeader("7"),
        ColumnHeader("8")
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
