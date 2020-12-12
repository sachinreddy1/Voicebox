package com.sachinreddy.feature.table.ui

import android.view.View
import androidx.databinding.DataBindingUtil
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.data.table.ColumnHeader
import com.sachinreddy.feature.databinding.TableViewColumnHeaderLayoutBinding
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.viewModel.AppViewModel

class ColumnHeaderViewHolder(
    layout: View,
    private val appViewModel: AppViewModel,
    private val editCellAdapter: EditCellAdapter
) : AbstractViewHolder(layout) {
    private var _columnHeader: ColumnHeader? = null

    var columnHeader: ColumnHeader?
        set(value) {
            _columnHeader = value
            binding?.columnHeader = value

            binding?.columnHeaderView?.apply {
                setOnClickListener {
                    if (appViewModel.isSelecting) {
                        editCellAdapter.clearSelectedCells()
                        for (i in 0..appViewModel.rowHeaders.value!!.size) {
                            editCellAdapter.getCellItem(value!!.columnPosition, i)?.let {
                                it.isSelected = true
                                appViewModel.selectedCells.add(it)
                            }
                        }

                        editCellAdapter.notifyDataSetChanged()
                    }
                }
            }

            binding?.executePendingBindings()
        }
        get() = _columnHeader

    val binding: TableViewColumnHeaderLayoutBinding? = try { DataBindingUtil.bind(itemView) } catch (t: Throwable) { null }
}