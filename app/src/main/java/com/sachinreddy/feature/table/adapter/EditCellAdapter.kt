package com.sachinreddy.feature.table.adapter

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.Track
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.data.table.RowHeader
import com.sachinreddy.feature.data.table.TimelineHeader
import com.sachinreddy.feature.databinding.TableViewCellLayoutBinding
import com.sachinreddy.feature.table.ui.CellViewHolder
import com.sachinreddy.feature.table.ui.ColumnHeaderViewHolder
import com.sachinreddy.feature.table.ui.RowHeaderViewHolder
import com.sachinreddy.feature.viewModel.AppViewModel
import javax.inject.Inject

class EditCellAdapter @Inject constructor(
    val context: Context,
    private val appViewModel: AppViewModel
) : AbstractTableAdapter<TimelineHeader?, RowHeader?, Cell?>() {

    var xPosition: Int = 0
    var isDragging: Boolean = false
    var isScrolling: Boolean = false
    private var scrollThread: Thread? = null

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: TableViewCellLayoutBinding = TableViewCellLayoutBinding.inflate(layoutInflater, parent, false)
        return CellViewHolder(binding, context, appViewModel, this)
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        (holder as CellViewHolder).apply {
            cellItemModel?.let { bind(it, rowPosition) }
        }
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder = ColumnHeaderViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_column_header_layout, parent, false),
        appViewModel,
        this
    )

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        timelineHeaderItemModel: TimelineHeader?,
        columnPosition: Int
    ) {
        (holder as ColumnHeaderViewHolder).apply {
            timelineHeaderItemModel?.let { bind(columnPosition) }
        }
    }

    override fun onCreateRowHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder = RowHeaderViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_row_header_layout, parent, false),
        appViewModel,
        this
    )

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeader?,
        rowPosition: Int
    ) {
        (holder as RowHeaderViewHolder).apply {
            bind(rowPosition)
        }
    }

    override fun onCreateCornerView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
        .inflate(R.layout.table_view_corner_layout, parent, false)

    override fun getColumnHeaderItemViewType(columnPosition: Int): Int = 0

    override fun getRowHeaderItemViewType(rowPosition: Int): Int = 0

    override fun getCellItemViewType(columnPosition: Int): Int = 0

    fun setTracks(tracks: MutableList<Track>) {
        var timelineHeaderList_: MutableList<TimelineHeader> = mutableListOf()
        val rowHeaderList_: MutableList<RowHeader> = mutableListOf()
        val cellList_: MutableList<MutableList<Cell>> = mutableListOf()
        tracks.map {
            timelineHeaderList_ = appViewModel.timelineHeaderList ?: mutableListOf()
            rowHeaderList_.add(it.rowHeader)
            cellList_.add(it.cellList ?: mutableListOf())
        }

        setAllItems(timelineHeaderList_.toList(), rowHeaderList_.toList(), cellList_.toList())
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

    fun startScrollThread() {
        val coordinates = IntArray(2)
        tableView.cellRecyclerView.getLocationOnScreen(coordinates)

        stopScrollThread()

        val threshold = 100
        val minWidth = coordinates.first()
        val maxWidth = minWidth + tableView.cellRecyclerView.width
        xPosition = tableView.cellRecyclerView.width/2

        scrollThread = object : Thread() {
            override fun run() {
                while (isDragging) {
                    if (xPosition > maxWidth - threshold) {
                        isScrolling = true
                        tableView.columnHeaderRecyclerView.scrollBy(10, 0)
                        tableView.cellLayoutManager.visibleCellRowRecyclerViews?.forEach {
                            it?.scrollBy(10, 0)
                        }
                    } else if (xPosition < minWidth + threshold) {
                        isScrolling = true
                        tableView.columnHeaderRecyclerView.scrollBy(-10, 0)
                        tableView.cellLayoutManager.visibleCellRowRecyclerViews?.forEach {
                            it?.scrollBy(-10, 0)
                        }
                    } else {
                        isScrolling = false
                    }
                    sleep(10)
                }
            }
        }

        isDragging = true
        scrollThread?.start()
    }

    fun stopScrollThread() {
        isDragging = false
        scrollThread?.join()
        scrollThread = null
    }

    fun clearSelectedCells() {
        appViewModel.selectedCells.clear()
        for (i in mCellItems) {
            for (j in i) {
                j?.apply {
                    isSelected = false
                    view?.setBackgroundColor(context.getColor(R.color.cardBackground))
                }
            }
        }
    }
}