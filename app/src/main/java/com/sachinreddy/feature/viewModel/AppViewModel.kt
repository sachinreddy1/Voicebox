package com.sachinreddy.feature.viewModel

import android.content.ClipData
import android.content.Context
import android.media.AudioManager
import android.media.AudioRecord
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.data.table.ColumnHeader
import com.sachinreddy.feature.data.table.RowHeader
import kotlinx.android.synthetic.main.operation_button.view.*
import javax.inject.Inject

class AppViewModel @Inject constructor(val context: Context) : ViewModel() {

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
                    track.map { cell ->
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
        val newCells = cells.value.orEmpty()

        newCells[cell.rowPosition][cell.columnPosition].apply {
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

        cells.postValue(newCells)
    }

    fun stopTrack(cell: Cell) {
        val newCells = cells.value.orEmpty()

        newCells[cell.rowPosition][cell.columnPosition].apply {
            isPlaying = false
            track?.pause()
            playerThread?.join()
        }

        cells.postValue(newCells)
    }

    fun onClickCellButton(cell: Cell) {
        if (cell.isPlaying)
            stopTrack(cell)
        else
            playTrack(cell)
    }

    fun onEditCellLongClicked(view: View, cell: Cell): Boolean {
        if (!isSelecting) {
//            editCellAdapter.startScrollThread()
//            stopTrack(cell)

            if (draggedCell == null) {
                draggedCell = cell
            }

            val data = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(view)
            view.startDragAndDrop(data, shadowBuilder, view, 0)
            view.visibility = View.INVISIBLE
        }
        return false
    }

    fun dropCell(cell: Cell) {
        val newCells = cells.value.orEmpty()

        draggedCell?.apply {
            newCells[cell.rowPosition][cell.columnPosition].data = data
            newCells[rowPosition][columnPosition].data = mutableListOf()
        }

        cells.postValue(newCells)
        draggedCell = null
    }

    fun toggleSelection(view: View) {
        isSelecting = if (isSelecting) {
            view.button_icon.setImageResource(R.drawable.ic_selection)
            view.button_icon.imageTintList = context.getColorStateList(R.color.whitesmoke)
            view.button_circle.visibility = View.INVISIBLE
            false
        } else {
            view.button_icon.setImageResource(R.drawable.ic_translation)
            view.button_icon.imageTintList = context.getColorStateList(R.color.operationBackground)
            view.button_circle.visibility = View.VISIBLE
            true
        }
    }
}