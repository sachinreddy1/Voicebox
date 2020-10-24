package com.sachinreddy.feature.table.ui

import android.content.ClipData
import android.content.Context
import android.os.VibrationEffect
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.table.listener.TranslationListener
import com.sachinreddy.feature.viewModel.AppViewModel


class CellViewHolder(
    layout: View,
    private val context: Context,
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
        edit_cell.setOnLongClickListener {
            editCellAdapter.startScrollThread()

            if (appViewModel.draggedCell == null){
                appViewModel.draggedCell = cell
            }

            cell.stopTrack()
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, shadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }

        // Long press for selection
        layout_cell.setOnLongClickListener {
            editCellAdapter.vibrate(90, VibrationEffect.EFFECT_HEAVY_CLICK)

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

        layout_cell.setOnDragListener(TranslationListener(context, editCellAdapter, appViewModel, cell))
    }
}