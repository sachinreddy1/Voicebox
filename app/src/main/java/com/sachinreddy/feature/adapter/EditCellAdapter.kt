package com.sachinreddy.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.Track
import com.sachinreddy.feature.table.Cell
import com.sachinreddy.feature.table.TimelineHeader
import com.sachinreddy.feature.table.RowHeader
import com.sachinreddy.feature.viewModel.AppViewModel
import kotlinx.android.synthetic.main.table_view_cell_layout.view.*


class EditCellAdapter(val context: Context, private val tracks: MutableList<Track>) : AbstractTableAdapter<TimelineHeader?, RowHeader?, Cell?>() {

    internal inner class MyCellViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        val cell_textview: TextView = itemView.findViewById(R.id.cell_data)
    }

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        // Get cell xml layout
        val layout: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_cell_layout, parent, false)
        // Create a Custom ViewHolder for a Cell item.
        return MyCellViewHolder(layout)
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        val cell = cellItemModel as Cell

        // Get the holder
        val viewHolder = holder as MyCellViewHolder
        viewHolder.cell_textview.text = cell.data.toString()

        // Set long click listener and touch event listener
        viewHolder.itemView.apply {
            setOnTouchListener { v, event ->
                when(event.action) {
                    MotionEvent.ACTION_MOVE -> {
                        println("${event.x}, ${event.y}")
                    }

                    MotionEvent.ACTION_DOWN -> {
                        selectCell(viewHolder.itemView)
                    }

                    MotionEvent.ACTION_HOVER_EXIT -> {
                        Toast.makeText(context, "OUTSIDE", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }
    }

    internal inner class MyColumnHeaderViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        val column_header_barNumber: TextView = itemView.findViewById(R.id.column_header_barNumber)
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {

        // Get Column Header xml Layout
        val layout: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_column_header_layout, parent, false)

        // Create a ColumnHeader ViewHolder
        return MyColumnHeaderViewHolder(layout)
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        timelineHeaderItemModel: TimelineHeader?,
        columnPosition: Int
    ) {
        val columnHeader = timelineHeaderItemModel as TimelineHeader

        // Get the holder to update cell item text
        val columnHeaderViewHolder =
            holder as MyColumnHeaderViewHolder
        columnHeaderViewHolder.column_header_barNumber.text = columnHeader.data.toString()
    }

    internal inner class MyRowHeaderViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        val row_header_imageView: ImageView = itemView.findViewById(R.id.row_header_imageView)
        val row_header_button_container: ConstraintLayout = itemView.findViewById(R.id.row_header_button_container)
        val row_header_button: ImageButton = itemView.findViewById(R.id.row_header_button)
    }

    override fun onCreateRowHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {

        // Get Row Header xml Layout
        val layout: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_row_header_layout, parent, false)

        // Create a Row Header ViewHolder
        return MyRowHeaderViewHolder(layout)
    }

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeader?,
        rowPosition: Int
    ) {
        val rowHeader = rowHeaderItemModel as RowHeader

        // Get the holder to update row header item text
        val rowHeaderViewHolder =
            holder as MyRowHeaderViewHolder

        rowHeaderViewHolder.row_header_button.apply {
            setOnClickListener {
                tracks.add(Track(RowHeader(""), 8, rowPosition))
                setTracks(tracks)
            }
        }

        // Set the add button at the bottom of the rowHeaders
        if (rowPosition == mRowHeaderItems.size - 1) {
            rowHeaderViewHolder.row_header_button_container.visibility = View.VISIBLE
        } else {
            rowHeaderViewHolder.row_header_button_container.visibility = View.GONE
        }
    }

    override fun onCreateCornerView(parent: ViewGroup): View {
        // Get Corner xml layout
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_corner_layout, parent, false)
    }

    override fun getColumnHeaderItemViewType(columnPosition: Int): Int {
        // The unique ID for this type of column header item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of ColumnViewHolder on "onCreateColumnViewHolder"
        return 0
    }

    override fun getRowHeaderItemViewType(rowPosition: Int): Int {
        // The unique ID for this type of row header item
        // If you have different items for Row Header View by Y (Row) position,
        // then you should fill this method to be able create different
        // type of RowHeaderViewHolder on "onCreateRowHeaderViewHolder"
        return 0
    }

    override fun getCellItemViewType(columnPosition: Int): Int {
        // The unique ID for this type of cell item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        return 0
    }

    fun setTracks(tracks: MutableList<Track>) {
        var timelineHeaderList_: MutableList<TimelineHeader> = mutableListOf()
        val rowHeaderList_: MutableList<RowHeader> = mutableListOf()
        val cellList_: MutableList<MutableList<Cell>> = mutableListOf()
        tracks.map {
            timelineHeaderList_ = it.timelineHeaderList ?: mutableListOf()
            rowHeaderList_.add(it.rowHeader)
            cellList_.add(it.cellList ?: mutableListOf())
        }

        setAllItems(timelineHeaderList_.toList(), rowHeaderList_.toList(), cellList_.toList())
    }

    private fun selectCell(
        view: View,
        top: Boolean = true,
        end: Boolean = true,
        bottom: Boolean = true,
        start: Boolean = true
    ) {
        view.apply {
            topSelection.visibility = if (top) View.VISIBLE else View.INVISIBLE
            endSelection.visibility = if (end) View.VISIBLE else View.INVISIBLE
            bottomSelection.visibility = if (bottom) View.VISIBLE else View.INVISIBLE
            startSelection.visibility = if (start) View.VISIBLE else View.INVISIBLE
        }
    }
}
