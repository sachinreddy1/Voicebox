package com.sachinreddy.feature.table.holder

import android.content.ClipData
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.table.listener.CellDragListener
import com.sachinreddy.feature.viewModel.AppViewModel


class CellViewHolder(
    layout: View,
    private val appViewModel: AppViewModel,
    private val editCellAdapter: EditCellAdapter
) : AbstractViewHolder(layout) {
    val cell_button: ImageButton = layout.findViewById(R.id.playStopButton)
    val selection_container: ConstraintLayout = layout.findViewById(R.id.selection_container)
    val edit_cell: ConstraintLayout = layout.findViewById(R.id.edit_cell)
    val layout_cell: ConstraintLayout = layout.findViewById(R.id.layout_cell)

    lateinit var cell: Cell

    fun bind(cell: Cell, rowPosition: Int, cellItems: MutableList<MutableList<Cell?>>) {
        this.cell = cell
        cell.let {
            it.rowPosition = rowPosition
            it.cellButton = cell_button
        }

        selection_container.visibility = if (cell.isSelected) View.VISIBLE else View.GONE
        edit_cell.visibility = if (cell.data.isNotEmpty()) View.VISIBLE else View.GONE

        cell_button.setOnClickListener {
            if (!cell.isPlaying)
                cell.playTrack()
            else
                cell.stopTrack()
        }

        // Long press for drag and drop
        val IMAGEVIEW_TAG = "icon bitmap"
        edit_cell.apply {
            tag = IMAGEVIEW_TAG
            setOnLongClickListener {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(it)
                it.startDrag(data, shadowBuilder, it, 0)
                it.visibility = View.INVISIBLE
                true
            }
        }

        // Long press for selection
        itemView.setOnLongClickListener {
            editCellAdapter.vibrate()

            // Un-selecting all the cells
            for (i in cellItems) {
                for (j in i) {
                    j?.isSelected = false
                }
            }

            // Select the new cell
            cell.isSelected = true
            appViewModel.selectedCell = cell
            editCellAdapter.notifyDataSetChanged()
            true
        }

        layout_cell.setOnDragListener(CellDragListener())
    }
}