package com.sachinreddy.feature.table.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.data.table.ColumnHeader
import com.sachinreddy.feature.data.table.RowHeader
import com.sachinreddy.feature.data.table.Timeline
import com.sachinreddy.feature.table.ui.CellViewHolder
import com.sachinreddy.feature.table.ui.ColumnHeaderViewHolder
import com.sachinreddy.feature.table.ui.RowHeaderViewHolder
import com.sachinreddy.feature.table.ui.TimelineViewHolder
import com.sachinreddy.feature.viewModel.AppViewModel
import com.sachinreddy.recyclerview.DiffUtil
import javax.inject.Inject

class EditCellAdapter @Inject constructor(
    val context: Context,
    private val appViewModel: AppViewModel
) : AbstractTableAdapter<Timeline?, ColumnHeader?, RowHeader?, Cell?>() {
    var cells: List<List<Cell>> = listOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(
                CellDiffCallback(
                    cells,
                    value
                ), true
            )
            field = value
            diff.dispatchUpdatesTo(cellRecyclerViewAdapter)
        }

    var rowHeaders: List<RowHeader> = listOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(
                RowHeaderDiffCallback(
                    rowHeaders,
                    value
                ), true
            )
            field = value
            diff.dispatchUpdatesTo(rowHeaderRecyclerViewAdapter)
        }

    var timelineHeaders: List<Timeline> = listOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(
                TimelineDiffCallback(
                    timelineHeaders,
                    value
                ), true
            )
            field = value
            diff.dispatchUpdatesTo(timelineRecyclerViewAdapter)
        }

    var columnHeaders: List<ColumnHeader> = listOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(
                ColumnHeaderDiffCallback(
                    columnHeaders,
                    value
                ), true
            )
            field = value
            diff.dispatchUpdatesTo(columnHeaderRecyclerViewAdapter)
        }

    // ------------------------------------------------- //

    class CellDiffCallback(val old: List<List<Cell>>, val updated: List<List<Cell>>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition].containsAll(updated[newItemPosition])

        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = updated.size
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition].containsAll(updated[newItemPosition])
    }

    class RowHeaderDiffCallback(val old: List<RowHeader>, val updated: List<RowHeader>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition].isEqual(updated[newItemPosition])

        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = updated.size
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition].isEqual(updated[newItemPosition])
    }

    class TimelineDiffCallback(val old: List<Timeline>, val updated: List<Timeline>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition].isEqual(updated[newItemPosition])

        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = updated.size
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition].isEqual(updated[newItemPosition])
    }

    class ColumnHeaderDiffCallback(val old: List<ColumnHeader>, val updated: List<ColumnHeader>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition].isEqual(updated[newItemPosition])

        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = updated.size
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition].isEqual(updated[newItemPosition])
    }

    // ------------------------------------------------- //

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder =
        CellViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.table_view_cell_layout, parent, false),
            context,
            appViewModel
        )

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        (holder as CellViewHolder).cell = cells[rowPosition][columnPosition]
    }

    override fun onCreateTimelineViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder = TimelineViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_timeline_layout, parent, false)
    )

    override fun onBindTimelineViewHolder(
        holder: AbstractViewHolder,
        timelineItemModel: Timeline?,
        columnPosition: Int
    ) {
        (holder as TimelineViewHolder).timeline = timelineHeaders[columnPosition]
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

    override fun getTimelineItemViewType(position: Int): Int = 0

    override fun getColumnHeaderItemViewType(columnPosition: Int): Int = 0

    override fun getRowHeaderItemViewType(rowPosition: Int): Int = 0

    override fun getCellItemViewType(columnPosition: Int): Int = 0

}