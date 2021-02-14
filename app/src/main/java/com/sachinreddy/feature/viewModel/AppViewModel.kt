package com.sachinreddy.feature.viewModel

import android.content.ClipData
import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evrencoskun.tableview.TableView
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.data.table.ColumnHeader
import com.sachinreddy.feature.data.table.RowHeader
import com.sachinreddy.feature.data.table.Timeline
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.table.listener.EditCellListener
import com.sachinreddy.feature.table.ui.shadow.UtilDragShadowBuilder
import com.sachinreddy.feature.util.toward
import com.sachinreddy.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.operation_button.view.*
import kotlinx.android.synthetic.main.table_view_cell_layout.view.*
import javax.inject.Inject

class AppViewModel @Inject constructor(val context: Context) : ViewModel() {

    var numberBars: MutableLiveData<Int> = MutableLiveData(8)

    lateinit var tableView: TableView
    var adapter: EditCellAdapter = EditCellAdapter(context, this)
    var tableViewListener: EditCellListener = EditCellListener(context)

    var audioManager: AudioManager? = null
    var recorder: AudioRecord? = null

    private var scrollThread: Thread? = null
    private var scrollHandler: Handler = Handler()

    var selectedCells: MutableList<Cell> = mutableListOf()
    var draggedCell: MutableLiveData<Cell?> = MutableLiveData(null)

    var isRecording: Boolean = false
    var isSelecting: Boolean = false
    var isScrolling: Boolean = false

    var bpm: MutableLiveData<Int> = MutableLiveData(120)

    private val recorderThread = object : Thread() {
        override fun run() {
            recordThread()
        }
    }

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

    var timelineHeaders: MutableLiveData<List<Timeline>> = MutableLiveData(
        listOf(
            Timeline(0),
            Timeline(1),
            Timeline(2),
            Timeline(3),
            Timeline(4),
            Timeline(5),
            Timeline(6),
            Timeline(7),
            Timeline(8),
            Timeline(9),
            Timeline(10),
            Timeline(11)
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

        initRecorder()
        recorderThread.start()
    }

    // ------------------------------------------------- //

    fun addTrack() {
        val trackCells: MutableList<Cell> = mutableListOf()
        numberBars.value?.let {
            for (i in 0 until it) {
                trackCells.add(
                    Cell(
                        columnPosition = i,
                        rowPosition = rowHeaders.value!!.size
                    )
                )
            }
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

    fun clearCellSelection(): List<List<Cell>> {
        selectedCells.clear()
        return cells.value.orEmpty().mapIndexed { rowPosition, track ->
            track.mapIndexed { columnPosition, cell ->
                val view = tableView.cellLayoutManager.getCellViewHolder(
                    columnPosition,
                    rowPosition
                )?.itemView?.layout_cell
                view?.setBackgroundColor(context.getColor(R.color.cardBackground))
                cell.isSelected = false
                cell
            }
            track
        }
    }

    fun selectCells(cell: Cell): List<List<Cell>> {
        val newCells = clearCellSelection()
        draggedCell.value?.apply {
            for (i in rowPosition toward cell.rowPosition) {
                for (j in columnPosition toward cell.columnPosition) {
                    newCells[i][j].isSelected = true
                    selectedCells.add(newCells[i][j])
                    val view =
                        tableView.cellLayoutManager.getCellViewHolder(j, i)?.itemView?.layout_cell
                    view?.setBackgroundColor(context.getColor(R.color.selection_color))
                }
            }
        }

        return newCells
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
//            stopTrack(cell)

            if (draggedCell.value == null) {
                draggedCell.postValue(cell)
            }

            val data = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(view)
            view.startDragAndDrop(data, shadowBuilder, view, 0)
            view.visibility = View.INVISIBLE
        }
        return false
    }

    fun onLayoutCellTouched(view: View, cell: Cell): Boolean {
        if (isSelecting) {
            draggedCell.postValue(cell)

            val data = ClipData.newPlainText("", "")
            val shadowBuilder = UtilDragShadowBuilder(view)
            view.startDragAndDrop(data, shadowBuilder, view, 0)
        }
        return false
    }

    fun dropCell(cell: Cell) {
        val newCells = cells.value.orEmpty()

        draggedCell.value?.apply {
            if (this != cell) {
                newCells[cell.rowPosition][cell.columnPosition].data = data
                newCells[rowPosition][columnPosition].data = mutableListOf()
            }
        }

        cells.postValue(newCells)
        draggedCell.postValue(null)
    }

    // ------------------------------------------------- //

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

        tableView.adapter?.notifyDataSetChanged()
    }

    fun toggleMetronome(view: View) {
        tableView.timelineRecyclerView.metronomeOn.value =
            if (tableView.timelineRecyclerView.metronomeOn.value!!) {
                view.button_icon.imageTintList = context.getColorStateList(R.color.whitesmoke)
                view.button_circle.visibility = View.INVISIBLE
                false
            } else {
                view.button_icon.imageTintList =
                    context.getColorStateList(R.color.operationBackground)
                view.button_circle.visibility = View.VISIBLE
                true
            }
    }

    fun toggleSmallFAB() {
        tableView.timelineRecyclerView.isPlaying.postValue(!tableView.timelineRecyclerView.isPlaying.value!!)
    }

    // ------------------------------------------------- //

    fun startScrolling(right: Boolean) {
        isScrolling = true
        scrollThread = Thread(ScrollRunner(right))
        scrollThread?.start()
    }

    fun stopScrolling() {
        isScrolling = false
        scrollThread?.join()
        scrollThread = null
    }

    // ------------------------------------------------- //

    fun startPlaying() {
        scrollThread = Thread(PlayRunner())
        scrollThread?.start()
    }

    fun stopPlaying() {
        scrollThread?.join()
        scrollThread = null
    }

    // ------------------------------------------------- //

    fun onProgressChanged(numberPicker: NumberPicker, progress: Int, fromUser: Boolean) {
        bpm.postValue(progress)
        tableView.timelineRecyclerView.showTimestamp(800)
    }

    // ------------------------------------------------- //

    private inner class ScrollRunner(right: Boolean) : Runnable {
        val translationValue = if (right) 20 else -20
        override fun run() {
            while (isScrolling) {
                scrollHandler.post {
                    tableView.timelineRecyclerView.scrollBy(translationValue, 0)
                }
                Thread.sleep(14)
            }
        }
    }

    private inner class PlayRunner() : Runnable {
        val barNumber =
            numberBars.value!!.toFloat() / tableView.timelineRecyclerView.mMaxTime.value!!
        val beatNumber = 4 * barNumber

        override fun run() {
            while (tableView.timelineRecyclerView.isPlaying.value!!) {
                val timeMS = beatNumber * (60000 / bpm.value!!)

                val time = tableView.timelineRecyclerView.mTime.value!!
                val maxTime = tableView.timelineRecyclerView.mMaxTime.value!!

                if (time == maxTime) {
                    tableView.timelineRecyclerView.isPlaying.postValue(false)
                    break
                }

                scrollHandler.post {
                    tableView.timelineRecyclerView.scrollBy(1, 0)
                }
                Thread.sleep(timeMS.toLong())
            }
        }
    }

    fun vibrate(duration: Long, effect: Int) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(
                VibrationEffect.createOneShot(
                    duration,
                    effect
                )
            )
        }
    }

    // ------------------------------------------------- //

    fun startRecording() {
        for (cell in selectedCells) {
            stopTrack(cell)
            cell.data.clear()
        }

        recorder?.startRecording()
        isRecording = true
    }

    fun stopRecording() {
        recorder?.stop()
        isRecording = false
    }

    private fun initRecorder() {
        val min = AudioRecord.getMinBufferSize(
            8000,
            AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        recorder = AudioRecord(
            MediaRecorder.AudioSource.VOICE_COMMUNICATION,
            8000,
            AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT,
            min
        )

        if (AcousticEchoCanceler.isAvailable()) {
            val echoCanceler = AcousticEchoCanceler.create(recorder!!.audioSessionId)
            echoCanceler.enabled = true
        }
    }

    private fun recordThread() {
        audioManager?.mode = AudioManager.MODE_IN_COMMUNICATION
        while (true) {
            if (isRecording) {
                val newCells = cells.value?.map { track ->
                    track.map { cell ->
                        if (cell.isSelected) {
                            val data = ShortArray(1024)
                            recorder?.read(data, 0, 1024)
                            cell.data.add(data)
                        }
                        cell
                    }
                    track
                }

                cells.postValue(newCells)
            }
        }
    }
}