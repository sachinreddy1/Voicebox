package com.sachinreddy.feature.table.ui

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.ColumnHeader
import com.sachinreddy.feature.databinding.TableViewColumnHeaderLayoutBinding
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.viewModel.AppViewModel

class ColumnHeaderViewHolder(
    layout: View,
    private val appViewModel: AppViewModel,
    private val editCellAdapter: EditCellAdapter
) : AbstractViewHolder(layout) {
    val column_header_container: ConstraintLayout = layout.findViewById(R.id.column_header_container)
    val column_header_barNumber: TextView = layout.findViewById(R.id.column_header_barNumber)

    private var _columnHeader: ColumnHeader? = null
    val binding: TableViewColumnHeaderLayoutBinding? = try { DataBindingUtil.bind(itemView) } catch (t: Throwable) { null }

    var columnHeader: ColumnHeader?
        set(value) {
            _columnHeader = value
            binding?.columnHeader = value

            column_header_container.apply {
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

//            column_header_barNumber.text = (_columnHeader?.columnPosition ?: 0 + 1).toString()

            binding?.executePendingBindings()
        }
        get() = _columnHeader
}