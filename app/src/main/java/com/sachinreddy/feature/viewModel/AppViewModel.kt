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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.data.*
import com.sachinreddy.feature.MainActivity
import com.sachinreddy.feature.R
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.table.listener.EditCellListener
import com.sachinreddy.feature.table.ui.shadow.UtilDragShadowBuilder
import com.sachinreddy.feature.util.Util
import com.sachinreddy.feature.util.toward
import com.sachinreddy.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.operation_button.view.*
import kotlinx.android.synthetic.main.table_view_cell_layout.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

private const val REQUEST_PERMISSION_CODE = 200
private val PERMISSIONS = arrayOf(
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    android.Manifest.permission.RECORD_AUDIO
)

class AppViewModel @Inject constructor(
    val context: Context,
    private val activity: MainActivity
) : ViewModel() {

    var numberBars: MutableLiveData<Int> = MutableLiveData(8)

    lateinit var tableView: TableView
    var adapter: EditCellAdapter = EditCellAdapter(context, this)
    var tableViewListener: EditCellListener = EditCellListener(context)

    var audioManager: AudioManager =
        activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    lateinit var recorder: AudioRecord

    var selectedCells: MutableList<Cell> = mutableListOf()
    var draggedCell: MutableLiveData<Cell?> = MutableLiveData(null)

    var isScrolling: Boolean = false
    var isRecording: Boolean = false
    var isSelecting: Boolean = false

    var bpm: MutableLiveData<Int> = MutableLiveData(120)
    var maxRecordTime: MutableLiveData<Int> = MutableLiveData(2000)
    var currentRecordIndex: MutableLiveData<Int> = MutableLiveData(0)

    private var scrollThread: Thread? = null
    private var scrollHandler: Handler = Handler()
    private var miscHandler: Handler = Handler()

    private val bufferSize = 1024

    // ------------------- INIT --------------------- //

    var cells: MutableLiveData<List<Track>> = MutableLiveData(
        listOf(
            Track(
                cells =
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

    init {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_PERMISSION_CODE)

        cells.value?.first()?.let { track ->
            track.cells.first().let { cell ->
                cell.isSelected = true
                selectedCells.clear()
                selectedCells.add(cell)
            }
        }

        initRecorder()
    }

    private fun initRecorder() {
        val min = AudioRecord.getMinBufferSize(
            8000,
            AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        recorder = AudioRecord(
            MediaRecorder.AudioSource.DEFAULT,
            8000,
            AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT,
            min
        )

        if (AcousticEchoCanceler.isAvailable()) {
            val echoCanceler = AcousticEchoCanceler.create(recorder.audioSessionId)
            echoCanceler.enabled = true
        }
    }

    // ------------------- SELECTION -------------------- //

    fun selectRow(rowPosition: Int) {
        if (isSelecting) {
            selectedCells.clear()

            val newCells = clearCellSelection().mapIndexed { index, track ->
                if (index == rowPosition) {
                    track.cells = track.cells.map { cell ->
                        val newCell = cell.copy()
                        newCell.isSelected = true
                        selectedCells.add(newCell)
                        newCell
                    }
                    track
                } else {
                    track
                }
            }

            setMaxRecordTime(
                selectedCells.first().columnPosition,
                selectedCells.last().columnPosition,
                bpm.value!!
            )

            cells.postValue(newCells)
        }
    }

    fun selectColumn(columnPosition: Int) {
        if (isSelecting) {
            selectedCells.clear()

            val newCells = clearCellSelection().map { track ->
                track.cells = track.cells.mapIndexed { index, cell ->
                    if (index == columnPosition) {
                        val newCell = cell.copy()
                        newCell.isSelected = true
                        selectedCells.add(newCell)
                        newCell
                    } else {
                        cell
                    }
                }
                track
            }

            setMaxRecordTime(
                selectedCells.first().columnPosition,
                selectedCells.last().columnPosition,
                bpm.value!!
            )

            cells.postValue(newCells)
        }
    }

    private fun clearCellSelection(): List<Track> {
        selectedCells.clear()
        return cells.value.orEmpty().mapIndexed { rowPosition, track ->
            track.cells = track.cells.mapIndexed { columnPosition, cell ->
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

    fun selectCells(cell: Cell): List<Track> {
        val newCells = clearCellSelection()
        draggedCell.value?.apply {
            for (i in rowPosition toward cell.rowPosition) {
                for (j in columnPosition toward cell.columnPosition) {
                    newCells[i].cells[j].let {
                        it.isSelected = true
                        selectedCells.add(it)
                    }
                    val view =
                        tableView.cellLayoutManager.getCellViewHolder(j, i)?.itemView?.layout_cell
                    view?.setBackgroundColor(context.getColor(R.color.selection_color))
                }
            }
        }

        setMaxRecordTime(
            selectedCells.first().columnPosition,
            selectedCells.last().columnPosition,
            bpm.value!!
        )

        return newCells
    }

    // ------------- TRANSLATION / SELECTION ----------------- //

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
                newCells[cell.rowPosition].cells[cell.columnPosition].data = data
                newCells[rowPosition].cells[columnPosition].data = mutableMapOf()
            }
        }

        cells.postValue(newCells)
        draggedCell.postValue(null)
        tableView.cellLayoutManager.visibleCellRowRecyclerViews?.get(cell.rowPosition)?.adapter?.notifyDataSetChanged()
    }

    // ------------------- OPERATIONS ------------------- //

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

        val track = Track(
            cells = trackCells
        )

        val newCells = cells.value!!.toMutableList()
        newCells.add(track)

        val newRowHeader = rowHeaders.value!!.toMutableList()
        newRowHeader.add(
            RowHeader(rowHeaders.value!!.size)
        )

        cells.postValue(newCells)
        rowHeaders.postValue(newRowHeader)
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

    // ------------------- THREADS ---------------------- //

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

    fun startPlaying() {
        scrollThread = Thread(PlayRunner())
        scrollThread?.start()
    }

    fun stopPlaying() {
        scrollThread?.join()
        scrollThread = null
    }

    fun startRecording() {
        isRecording = true

        val newCells = cells.value?.map { track ->
            track.apply {
                this.audioTrack?.pause()
                this.playerThread?.join()

                cells.map { cell ->
                    if (cell.isSelected) {
                        val newCell = cell.copy()
                        newCell.isPlaying = false
                        newCell.data.clear()

                        newCell
                    } else {
                        cell
                    }
                }
            }
        }

        cells.postValue(newCells)

        Thread(
            RecordTimelineRunner(
                selectedCells.first().columnPosition,
                selectedCells.last().columnPosition
            )
        ).start()
    }

    fun stopRecording() {
        recorder.stop()
        isRecording = false
    }

    // ------------------------------------------------- //

    fun onProgressChanged(numberPicker: NumberPicker, progress: Int, fromUser: Boolean) {
        bpm.postValue(progress)
        tableView.timelineRecyclerView.showTimestamp(800)
        setMaxRecordTime(
            selectedCells.first().columnPosition,
            selectedCells.last().columnPosition,
            bpm.value!!
        )
    }

    private fun setMaxRecordTime(startPosition: Int, endPosition: Int, bpm: Int) {
        val numBeats = ((endPosition - startPosition) + 1) * 4
        val millis = numBeats * (60000 / bpm)
        maxRecordTime.postValue(millis)
    }

    // ------------------------------------------------- //

    private fun recordData(currentTime: Int) {
        if ((tableView.timelineRecyclerView.computeHorizontalScrollRange() - tableView.timelineRecyclerView.computeHorizontalScrollExtent()) == 0)
            return

        numberBars.value!!.let { numberBars ->
            val barLength =
                (tableView.timelineRecyclerView.computeHorizontalScrollRange() - tableView.timelineRecyclerView.computeHorizontalScrollExtent()) / numberBars
            val barNumber = currentTime / barLength

            println(barNumber)

            val data = ShortArray(bufferSize)
            val result = recorder.read(data, 0, bufferSize)

//                println(data.map { abs(it.toInt()) }.sum())

            if (result > 0) {
//                    val newCells = cells.value?.map { track ->
//                        track.cells = track.cells.mapIndexed { index, cell ->
//                            if (cell.isSelected && barNumber == index) {
//                                val newCell = cell.copy()
//
//                                println("OBSERVER: ${newCell.data.size}")
//
//                                newCell.data[currentTime] = data
//                                newCell
//                            } else {
//                                cell
//                            }
//                        }
//
//                        track
//                    }
//
//                    cells.postValue(newCells)
            }
        }
    }

    /*
     * [RecordTimelineRunner]
     * Used when record button is pressed.
     * Responsible for scrolling the timelineRecyclerView based on selected cells.
     *
     */
    private inner class RecordTimelineRunner(startPosition: Int, endPosition: Int) : Runnable {
        val barLength =
            (tableView.timelineRecyclerView.computeHorizontalScrollRange() - tableView.timelineRecyclerView.computeHorizontalScrollExtent()) / numberBars.value!!

        val initialX = tableView.timelineRecyclerView.computeHorizontalScrollOffset()
        val startX = barLength * startPosition
        val endX = barLength * (endPosition + 1)

        val barNumber =
            numberBars.value!!.toFloat() / tableView.timelineRecyclerView.mMaxTime.value!!
        val beatNumber = 4 * barNumber

        val timeMS = (beatNumber * (60000 / bpm.value!!))
        val timeNS = (timeMS * 1000000).toLong()

        override fun run() {
            audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
            recorder.startRecording()

            // Go to start position
            for (i in 0..(abs(startX - initialX))) {
                scrollHandler.post {
                    tableView.timelineRecyclerView.scrollBy(if (startX > initialX) 1 else -1, 0)
                }
            }

            // Scroll through selected
            for (i in 0..(abs(endX - startX))) {
                if (!isRecording) break

                val startTime = System.nanoTime()

                viewModelScope.launch {
                    tableView.timelineRecyclerView.scrollBy(1, 0)
                    recordData(tableView.timelineRecyclerView.computeHorizontalScrollOffset())
                }

                Util.sleepNano(startTime, timeNS)
            }
        }
    }

    /*
     * [PlayRunner]
     * Used when play button is pressed.
     * Responsible for scrolling the timelineRecyclerView.
     */
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

    /*
     * [ScrollRunner]
     * Used when translating/selecting.
     * Scrolls recyclerView left or right.
     */
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

    // ------------------------------------------------- //

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
}