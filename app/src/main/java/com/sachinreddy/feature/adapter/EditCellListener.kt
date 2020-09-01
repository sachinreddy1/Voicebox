package com.sachinreddy.feature.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.listener.ITableViewListener
import com.sachinreddy.feature.data.table.Cell
import kotlinx.android.synthetic.main.table_view_cell_layout.view.*

class EditCellListener(val context: Context, val tableView: TableView) : ITableViewListener {
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
        // Do what you want.
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
        // Do what you want.
    }

    override fun onRowHeaderLongPressed(
        rowHeaderView: RecyclerView.ViewHolder,
        rowPosition: Int
    ) {
        // Do what you want.
    }
}