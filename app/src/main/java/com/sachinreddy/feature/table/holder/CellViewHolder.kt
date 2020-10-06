package com.sachinreddy.feature.table.holder

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.viewModel.AppViewModel


class CellViewHolder(
    layout: View,
    private val appViewModel: AppViewModel,
    private val editCellAdapter: EditCellAdapter
) : AbstractViewHolder(layout) {
    val cell_button: ImageButton = layout.findViewById(R.id.playStopButton)
    val selection_container: ConstraintLayout = layout.findViewById(R.id.selection_container)
    val edit_cell: ConstraintLayout = layout.findViewById(R.id.edit_cell)

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
                val item = ClipData.Item(it.tag as? CharSequence)
                val data = ClipData(it.tag as? CharSequence, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item)
                val shadowBuilder = DragShadowBuilder(it)
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

        itemView.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    if (event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        println("SACHIN")
                        true
                    }
                    false
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.background.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
                    v.invalidate()
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION ->
                    true
                DragEvent.ACTION_DRAG_EXITED -> {
                    v.background.clearColorFilter()
                    v.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    // Gets the item containing the dragged data
                    val item: ClipData.Item = event.clipData.getItemAt(0)

                    // Gets the text data from the item.
                    val dragData = item.text

                    // Displays a message containing the dragged data.
                    println("REDDY")

                    // Turns off any color tints
                    v.background.clearColorFilter()

                    // Invalidates the view to force a redraw
                    v.invalidate()

                    // Returns true. DragEvent.getResult() will return true.
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    // Turns off any color tinting
                    v.background.clearColorFilter()

                    // Invalidates the view to force a redraw
                    v.invalidate()

                    // Does a getResult(), and displays what happened.
                    when(event.result) {
                        true ->
                            println("The drop was handled.")
                        else ->
                            println("The drop didn't work.")
                    }

                    // returns true; the value is ignored.
                    true
                }
                else -> {
                    // An unknown action type was received.
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.")
                    false
                }
            }
            false
        }
    }
}