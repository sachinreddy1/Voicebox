package com.sachinreddy.feature.table.ui

import android.content.ClipData
import android.view.View
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
    private val appViewModel: AppViewModel,
    private val editCellAdapter: EditCellAdapter
) : AbstractViewHolder(itemView) {
    val edit_cell: ConstraintLayout = itemView.findViewById(R.id.edit_cell)
    val layout_cell: ConstraintLayout = itemView.findViewById(R.id.layout_cell)

    private var _cell: Cell? = null

    var cell: Cell?
        set(value) {
            _cell = value

            binding?.cell = value
            binding?.vm = appViewModel

            // TRANSLATION
            edit_cell.setOnLongClickListener {
                if (!appViewModel.isSelecting) {
                    editCellAdapter.startScrollThread()

                    if (appViewModel.draggedCell == null) {
                        _cell?.let { appViewModel.draggedCell = it }
                    }

                    _cell?.let { appViewModel.stopTrack(it) }
                    val data = ClipData.newPlainText("", "")
                    val shadowBuilder = View.DragShadowBuilder(it)
                    it.startDragAndDrop(data, shadowBuilder, it, 0)
                    it.visibility = View.INVISIBLE
                }
                true
            }

            // SELECTION
            layout_cell.setOnTouchListener { v, event ->
                if (appViewModel.isSelecting) {
                    _cell?.let { appViewModel.startingCell = it }

                    editCellAdapter.clearSelectedCells()
                    editCellAdapter.startScrollThread()
                    val data = ClipData.newPlainText("", "")
                    val shadowBuilder = UtilDragShadowBuilder(v)
                    v.startDragAndDrop(data, shadowBuilder, v, 0)
                }
                true
            }

            // DRAG LISTENERS
            layout_cell.apply {
                if (appViewModel.isSelecting)
                    _cell?.let {
                        setOnDragListener(
                            SelectionListener(
                                context,
                                editCellAdapter,
                                appViewModel,
                                it
                            )
                        )
                    }
                else
                    _cell?.let {
                        setOnDragListener(
                            TranslationListener(
                                context,
                                editCellAdapter,
                                appViewModel,
                                it
                            )
                        )
                    }
            }

            binding?.executePendingBindings()
        }
        get() = _cell

    private val binding: TableViewCellLayoutBinding? = try { DataBindingUtil.bind(itemView) } catch (t: Throwable) { null }
}