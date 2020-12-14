package com.sachinreddy.feature.table.ui

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.databinding.TableViewCellLayoutBinding
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.viewModel.AppViewModel


class CellViewHolder(
    itemView: View,
    private val appViewModel: AppViewModel,
    private val editCellAdapter: EditCellAdapter
) : AbstractViewHolder(itemView) {
    val layout_cell: ConstraintLayout = itemView.findViewById(R.id.layout_cell)

    private var _cell: Cell? = null

    var cell: Cell?
        set(value) {
            _cell = value

            binding?.cell = value
            binding?.vm = appViewModel

            binding?.editCell?.apply {
                binding.vm = appViewModel
                binding.cell = value
            }

            // SELECTION
//            layout_cell.setOnTouchListener { v, event ->
//                if (appViewModel.isSelecting) {
//                    _cell?.let { appViewModel.startingCell = it }
//
//                    editCellAdapter.clearSelectedCells()
//                    editCellAdapter.startScrollThread()
//                    val data = ClipData.newPlainText("", "")
//                    val shadowBuilder = UtilDragShadowBuilder(v)
//                    v.startDragAndDrop(data, shadowBuilder, v, 0)
//                }
//                true
//            }

            // DRAG LISTENERS
//            layout_cell.apply {
//                if (appViewModel.isSelecting)
//                    _cell?.let {
//                        setOnDragListener(
//                            SelectionListener(
//                                context,
//                                editCellAdapter,
//                                appViewModel,
//                                it
//                            )
//                        )
//                    }
//                else
//                    _cell?.let {
//                        setOnDragListener(
//                            TranslationListener(
//                                context,
//                                editCellAdapter,
//                                appViewModel,
//                                it
//                            )
//                        )
//                    }
//            }

            binding?.executePendingBindings()
        }
        get() = _cell

    private val binding: TableViewCellLayoutBinding? = try { DataBindingUtil.bind(itemView) } catch (t: Throwable) { null }
}