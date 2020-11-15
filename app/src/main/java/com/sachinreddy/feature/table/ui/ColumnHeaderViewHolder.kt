package com.sachinreddy.feature.table.ui

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.viewModel.AppViewModel

class ColumnHeaderViewHolder(
    layout: View,
    private val appViewModel: AppViewModel,
    private val editCellAdapter: EditCellAdapter
) : AbstractViewHolder(layout) {
    val column_header_container: ConstraintLayout = layout.findViewById(R.id.column_header_container)
    val column_header_barNumber: TextView = layout.findViewById(R.id.column_header_barNumber)

    fun bind(columnPosition: Int) {
        column_header_container.apply {
            setOnClickListener {
                if (appViewModel.isSelecting) {
                    editCellAdapter.clearSelectedCells()
                    for (i in 0..appViewModel.mTrackList.size) {
                        editCellAdapter.getCellItem(columnPosition, i)?.let {
                            it.isSelected = true
                            appViewModel.selectedCells.add(it)
                        }
                    }

                    editCellAdapter.notifyDataSetChanged()
                }
            }
        }

        column_header_barNumber.text = (columnPosition + 1).toString()
    }
}