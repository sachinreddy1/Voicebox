package com.sachinreddy.feature.table.adapter

import android.content.Context
import android.media.*
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
import com.sachinreddy.feature.table.holder.CellViewHolder
import com.sachinreddy.feature.table.holder.ColumnHeaderViewHolder
import com.sachinreddy.feature.table.holder.RowHeaderViewHolder
import com.sachinreddy.feature.viewModel.AppViewModel

class EditCellAdapter(
    val context: Context,
    private val appViewModel: AppViewModel
) : AbstractTableAdapter<TimelineHeader?, RowHeader?, Cell?>() {

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder =
        CellViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.table_view_cell_layout, parent, false),
            context,
            appViewModel,
            this
        )

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        // Get the holder
        (holder as CellViewHolder).apply {
            cellItemModel?.let { bind(it, rowPosition, mCellItems) }
        }
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder = ColumnHeaderViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_column_header_layout, parent, false)
    )

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        timelineHeaderItemModel: TimelineHeader?,
        columnPosition: Int
    ) {
        val columnHeader = timelineHeaderItemModel as TimelineHeader

        // Get the holder to update cell item text
        val columnHeaderViewHolder =
            holder as ColumnHeaderViewHolder
        columnHeaderViewHolder.column_header_barNumber.text = columnHeader.data.toString()
    }

    override fun onCreateRowHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder = RowHeaderViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_row_header_layout, parent, false)
    )

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeader?,
        rowPosition: Int
    ) {
        val rowHeader = rowHeaderItemModel as RowHeader

        // Get the holder to update row header item text
        val rowHeaderViewHolder =
            holder as RowHeaderViewHolder

        rowHeaderViewHolder.row_header_button.apply {
            setOnClickListener {
                appViewModel.mTrackList.add(
                    Track(
                        RowHeader(""),
                        appViewModel.numberBars,
                        rowPosition + 1
                    )
                )
                setTracks(appViewModel.mTrackList)
            }
        }

        // Set the add button at the bottom of the rowHeaders
        if (rowPosition == mRowHeaderItems.size - 1) {
            rowHeaderViewHolder.row_header_button_container.visibility = View.VISIBLE
        } else {
            rowHeaderViewHolder.row_header_button_container.visibility = View.GONE
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
}
