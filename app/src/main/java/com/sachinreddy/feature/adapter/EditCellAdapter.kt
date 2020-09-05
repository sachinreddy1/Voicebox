package com.sachinreddy.feature.adapter

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.Track
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.data.table.RowHeader
import com.sachinreddy.feature.data.table.TimelineHeader
import com.sachinreddy.feature.viewModel.AppViewModel


class EditCellAdapter(val context: Context, val appViewModel: AppViewModel, private val tracks: MutableList<Track>) : AbstractTableAdapter<TimelineHeader?, RowHeader?, Cell?>() {
    var selectedCell: Cell? = null

    internal inner class MyCellViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        val cell_textview: TextView = itemView.findViewById(R.id.cell_data)
        val selection_container: ConstraintLayout = itemView.findViewById(R.id.selection_container)
        val edit_cell: ConstraintLayout = itemView.findViewById(R.id.edit_cell)
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

        cell.rowPosition = rowPosition

        // Get the holder
        val viewHolder = holder as MyCellViewHolder
        viewHolder.apply {
            cell_textview.text = cell.data.toString()
            selection_container.visibility = if (cell.isSelected) View.VISIBLE else View.GONE
            edit_cell.visibility = if (cell.isSelected) View.VISIBLE else View.GONE

            // Long press for selection
            itemView.setOnLongClickListener {
                // Add short vibration
                val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(
                        VibrationEffect.createOneShot(
                            100,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                }

                // Un-selecting all the cells
                for (i in mCellItems) {
                    for (j in i) {
                        j?.isSelected = false
                    }
                }

                // Select the new cell
                cell.isSelected = true
                selectedCell = cell
                notifyDataSetChanged()
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
                tracks.add(Track(RowHeader(""), appViewModel.numberBars, rowPosition + 1))
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
            timelineHeaderList_ = appViewModel.timelineHeaderList ?: mutableListOf()
            rowHeaderList_.add(it.rowHeader)
            cellList_.add(it.cellList ?: mutableListOf())
        }

        setAllItems(timelineHeaderList_.toList(), rowHeaderList_.toList(), cellList_.toList())
    }
}
