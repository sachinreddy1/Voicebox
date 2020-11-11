package com.sachinreddy.feature.table.listener

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.evrencoskun.tableview.listener.ITableViewListener
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.viewModel.AppViewModel

class EditCellListener(
    val context: Context,
    val appViewModel: AppViewModel,
    private val editCellAdapter: EditCellAdapter
) : ITableViewListener {
    override fun onCellClicked(
        cellView: RecyclerView.ViewHolder,
        columnPosition: Int,
        rowPosition: Int
    ) {
        // Do what you want.
    }

    override fun onCellLongPressed(
        cellView: RecyclerView.ViewHolder,
        column: Int,
        row: Int
    ) {
        // Do what you want.
    }

    override fun onColumnHeaderClicked(
        columnHeaderView: RecyclerView.ViewHolder,
        columnPosition: Int
    ) {
        if (!appViewModel.isSelecting) return

        editCellAdapter.clearSelectedCells()
        for (i in 0..appViewModel.mTrackList.size) {
            val cell = editCellAdapter.getCellItem(columnPosition, i)
            cell?.let {
                it.isSelected = true
            }
        }

        editCellAdapter.notifyDataSetChanged()
    }

    override fun onColumnHeaderLongPressed(
        columnHeaderView: RecyclerView.ViewHolder,
        columnPosition: Int
    ) {
        // Do what you want.
    }

    override fun onRowHeaderClicked(
        rowHeaderView: RecyclerView.ViewHolder,
        rowPosition: Int
    ) {
        if (!appViewModel.isSelecting) return

        editCellAdapter.clearSelectedCells()
        for (i in 0..appViewModel.numberBars) {
            val cell = editCellAdapter.getCellItem(i, rowPosition)
            cell?.let {
                it.isSelected = true
            }
        }

        editCellAdapter.notifyDataSetChanged()
    }

    override fun onRowHeaderLongPressed(
        rowHeaderView: RecyclerView.ViewHolder,
        rowPosition: Int
    ) {
        // Do what you want.
    }
}