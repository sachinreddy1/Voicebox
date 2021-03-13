package com.sachinreddy.feature.table.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.evrencoskun.tableview.data.Cell
import com.evrencoskun.tableview.data.ColumnHeader
import com.evrencoskun.tableview.data.RowHeader
import com.evrencoskun.tableview.data.Timeline
import com.sachinreddy.feature.R
import com.sachinreddy.feature.table.ui.CellViewHolder
import com.sachinreddy.feature.table.ui.ColumnHeaderViewHolder
import com.sachinreddy.feature.table.ui.RowHeaderViewHolder
import com.sachinreddy.feature.table.ui.TimelineViewHolder
import com.sachinreddy.feature.viewModel.AppViewModel
import javax.inject.Inject

class EditCellAdapter @Inject constructor(
    val context: Context,
    private val appViewModel: AppViewModel
) : AbstractTableAdapter<Timeline?, ColumnHeader?, RowHeader?, Cell?>() {

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
        (holder as CellViewHolder).cell = cellItemModel
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
        (holder as TimelineViewHolder).timeline = timelineItemModel
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
        (holder as ColumnHeaderViewHolder).columnHeader = columnHeaderItemModel
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
        (holder as RowHeaderViewHolder).rowHeader = rowHeaderItemModel
    }

    override fun onCreateCornerView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
        .inflate(R.layout.table_view_corner_layout, parent, false)

    override fun getTimelineItemViewType(position: Int): Int = 0

    override fun getColumnHeaderItemViewType(columnPosition: Int): Int = 0

    override fun getRowHeaderItemViewType(rowPosition: Int): Int = 0

    override fun getCellItemViewType(columnPosition: Int): Int = 0

}