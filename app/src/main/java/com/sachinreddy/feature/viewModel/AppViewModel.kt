package com.sachinreddy.feature.viewModel

import android.media.AudioManager
import android.media.AudioRecord
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.data.table.ColumnHeader
import com.sachinreddy.feature.data.table.RowHeader
import javax.inject.Inject

class AppViewModel @Inject constructor() : ViewModel() {

    var numberBars: Int = 8

    var startingCell: Cell? = null
    var selectedCells: MutableList<Cell> = mutableListOf()
    var draggedCell: Cell? = null

    var audioManager: AudioManager? = null
    var recorder: AudioRecord? = null

    var isRecording = false
    var isSelecting: Boolean = false

    // ------------------------------------------------- //

    var cells: MutableLiveData<List<List<Cell>>> = MutableLiveData(
        listOf(
            listOf(
                Cell(columnPosition = 0, rowPosition = 0),
                Cell(columnPosition = 1, rowPosition = 0),
                Cell(columnPosition = 2, rowPosition = 0),
                Cell(columnPosition = 3, rowPosition = 0),
                Cell(columnPosition = 4, rowPosition = 0),
                Cell(columnPosition = 5, rowPosition = 0),
                Cell(columnPosition = 6, rowPosition = 0),
                Cell(columnPosition = 7, rowPosition = 0)
            )
        )
    )

    var rowHeaders: MutableLiveData<List<RowHeader>> = MutableLiveData(
        listOf(
            RowHeader(0)
        )
    )

    var columnHeaders: MutableLiveData<List<ColumnHeader>> = MutableLiveData(
        listOf(
            ColumnHeader(0),
            ColumnHeader(1),
            ColumnHeader(2),
            ColumnHeader(3),
            ColumnHeader(4),
            ColumnHeader(5),
            ColumnHeader(6),
            ColumnHeader(7)
        )
    )

    // ------------------------------------------------- //

    init {
        cells.value?.first()?.first()?.let {
            it.isSelected = true
            selectedCells.clear()
            selectedCells.add(it)
        }
    }

    // ------------------------------------------------- //

    fun addTrack() {
        val trackCells: MutableList<Cell> = mutableListOf()
        for (i in 0 until numberBars) {
            trackCells.add(
                Cell(
                    columnPosition = i,
                    rowPosition = rowHeaders.value!!.size
                )
            )
        }

        val newCells = cells.value!!.toMutableList()
        newCells.add(trackCells)

        val newRowHeader = rowHeaders.value!!.toMutableList()
        newRowHeader.add(
            RowHeader(rowHeaders.value!!.size)
        )

        cells.postValue(newCells)
        rowHeaders.postValue(newRowHeader)
    }

    fun selectRow(rowPosition: Int) {
        if (isSelecting) {
            val newCells = cells.value?.mapIndexed { index, track ->
                if (index == rowPosition) {
                    track.map {cell ->
                        cell.isSelected = true
                        cell
                    }
                } else {
                    track.map { cell ->
                        cell.isSelected = false
                        cell
                    }
                }
                track
            }

            cells.postValue(newCells)
        }
    }

    // ------------------------------------------------- //

    fun selectColumn(columnPosition: Int) {
        if (isSelecting) {
            val newCells = cells.value?.map { track ->
                track.mapIndexed { index, cell ->
                    cell.isSelected = (index == columnPosition)
                    cell
                }
                track
            }

            cells.postValue(newCells)
        }
    }

    // ------------------------------------------------- //

    fun playTrack(cell: Cell) {
        val newCells = cells.value?.mapIndexed { rowPosition, tracks ->
            if (rowPosition == cell.rowPosition) {
                tracks.mapIndexed { columnPosition, newCell ->
                    if (columnPosition == cell.columnPosition) {
                        newCell.apply {
                            playerThread = object : Thread() {
                                override fun run() {
                                    while (isPlaying) {
                                        data.forEach {
                                            track?.write(it, 0, 1024)
                                        }
                                    }
                                }
                            }
                            isPlaying = true
                            playerThread?.start()
                            track?.play()
                        }
                    }
                    newCell
                }
            }
            tracks
        }

        cells.postValue(newCells)
    }

    fun stopTrack(cell: Cell) {
        val newCells = cells.value?.mapIndexed { rowPosition, tracks ->
            if (rowPosition == cell.rowPosition) {
                tracks.mapIndexed { columnPosition, newCell ->
                    if (columnPosition == cell.columnPosition) {
                        newCell.apply {
                            isPlaying = false
                            track?.pause()
                            playerThread?.join()
                        }
                    }
                    newCell
                }
            }
            tracks
        }

        cells.postValue(newCells)
    }

    fun onClickCellButton(cell: Cell) {
        if (cell.isPlaying)
            stopTrack(cell)
        else
            playTrack(cell)
    }
}