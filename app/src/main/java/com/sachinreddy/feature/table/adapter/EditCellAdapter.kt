package com.sachinreddy.feature.table.adapter

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.data.table.ColumnHeader
import com.sachinreddy.feature.data.table.RowHeader
import com.sachinreddy.feature.table.ui.CellViewHolder
import com.sachinreddy.feature.table.ui.ColumnHeaderViewHolder
import com.sachinreddy.feature.table.ui.RowHeaderViewHolder
import com.sachinreddy.feature.viewModel.AppViewModel
import javax.inject.Inject

class EditCellAdapter @Inject constructor(
    val context: Context,
    private val appViewModel: AppViewModel
) : AbstractTableAdapter<ColumnHeader?, RowHeader?, Cell?>() {

    var xPosition: Int = 0
    var isDragging: Boolean = false
    var isScrolling: Boolean = false
    private var scrollThread: Thread? = null

    // ------------------------------------------------- //

    var cells : List<List<Cell>> = listOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(
                CellDiffCallback(
                    cells,
                    value
                ), true)
            field = value
            diff.dispatchUpdatesTo(cellRecyclerViewAdapter)
        }

    var rowHeaders : List<RowHeader> = listOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(
                RowHeaderDiffCallback(
                    rowHeaders,
                    value
                ), true)
            field = value
            diff.dispatchUpdatesTo(rowHeaderRecyclerViewAdapter)
        }

    var columnHeaders : List<ColumnHeader> = listOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(
                ColumnHeaderDiffCallback(
                    columnHeaders,
                    value
                ), true)
            field = value
            diff.dispatchUpdatesTo(columnHeaderRecyclerViewAdapter)
        }

    // ------------------------------------------------- //

    class CellDiffCallback(val old: List<List<Cell>>, val updated: List<List<Cell>>): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = old[oldItemPosition].containsAll(updated[newItemPosition])
        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = updated.size
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = old[oldItemPosition] == updated[newItemPosition]
    }

    class RowHeaderDiffCallback(val old: List<RowHeader>, val updated: List<RowHeader>): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = old[oldItemPosition] == updated[newItemPosition]
        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = updated.size
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = old[oldItemPosition] == updated[newItemPosition]
    }

    class ColumnHeaderDiffCallback(val old: List<ColumnHeader>, val updated: List<ColumnHeader>): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = old[oldItemPosition] == updated[newItemPosition]
        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = updated.size
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = old[oldItemPosition] == updated[newItemPosition]
    }

    // ------------------------------------------------- //

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder =
        CellViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.table_view_cell_layout, parent, false),
            appViewModel,
            this
        )

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        (holder as CellViewHolder).cell = cells[rowPosition][columnPosition]
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder = ColumnHeaderViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_column_header_layout, parent, false),
        appViewModel
    )

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: ColumnHeader?,
        columnPosition: Int
    ) {
        (holder as ColumnHeaderViewHolder).columnHeader = columnHeaders[columnPosition]
    }

    override fun onCreateRowHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder = RowHeaderViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_row_header_layout, parent, false),
        appViewModel
    )

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeader?,
        rowPosition: Int
    ) {
        (holder as RowHeaderViewHolder).rowHeader = rowHeaders[rowPosition]
    }

    override fun onCreateCornerView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
        .inflate(R.layout.table_view_corner_layout, parent, false)

    override fun getColumnHeaderItemViewType(columnPosition: Int): Int = 0

    override fun getRowHeaderItemViewType(rowPosition: Int): Int = 0

    override fun getCellItemViewType(columnPosition: Int): Int = 0

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
//                    view?.setBackgroundColor(context.getColor(R.color.cardBackground))
                }
            }
        }
    }
}