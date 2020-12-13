package com.sachinreddy.feature.table.ui

import android.content.ClipData
import android.content.Context
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.databinding.TableViewCellLayoutBinding
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.table.listener.SelectionListener
import com.sachinreddy.feature.table.listener.TranslationListener
import com.sachinreddy.feature.table.ui.shadow.UtilDragShadowBuilder
import com.sachinreddy.feature.viewModel.AppViewModel


class CellViewHolder(
    itemView: View,
    private val context: Context,
    private val appViewModel: AppViewModel,
    private val editCellAdapter: EditCellAdapter
) : AbstractViewHolder(itemView) {
    val cell_button: ImageButton = itemView.findViewById(R.id.action_button)
    val edit_cell: ConstraintLayout = itemView.findViewById(R.id.edit_cell)
    val layout_cell: ConstraintLayout = itemView.findViewById(R.id.layout_cell)

    private var _cell: Cell? = null

    var cell: Cell?
        set(value) {
            _cell = value
            binding?.cell = value

            _cell?.let {cell ->
                cell.cellButton = cell_button
                cell.view = layout_cell

                cell_button.setOnClickListener {
                    if (!cell.isPlaying)
                        cell.playTrack()
                    else
                        cell.stopTrack()
                }

                // Long press for drag and drop
                edit_cell.setOnLongClickListener {
                    if (!appViewModel.isSelecting) {
                        editCellAdapter.startScrollThread()

                        if (appViewModel.draggedCell == null) {
                            appViewModel.draggedCell = cell
                        }

                        cell.stopTrack()
                        val data = ClipData.newPlainText("", "")
                        val shadowBuilder = View.DragShadowBuilder(it)
                        it.startDragAndDrop(data, shadowBuilder, it, 0)
                        it.visibility = View.INVISIBLE
                    }
                    true
                }

                // Touch for selection
                layout_cell.setOnTouchListener { v, event ->
                    if (appViewModel.isSelecting) {
                        appViewModel.startingCell = cell

                        editCellAdapter.clearSelectedCells()
                        editCellAdapter.startScrollThread()
                        val data = ClipData.newPlainText("", "")
                        val shadowBuilder = UtilDragShadowBuilder(v)
                        v.startDragAndDrop(data, shadowBuilder, v, 0)
                    }
                    true
                }

                layout_cell.apply {
                    if (appViewModel.isSelecting)
                        setOnDragListener(SelectionListener(context, editCellAdapter, appViewModel, cell))
                    else
                        setOnDragListener(TranslationListener(context, editCellAdapter, appViewModel, cell))
                }
            }


            binding?.executePendingBindings()
        }
        get() = _cell

    private val binding: TableViewCellLayoutBinding? = try { DataBindingUtil.bind(itemView) } catch (t: Throwable) { null }
}