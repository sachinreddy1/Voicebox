package com.sachinreddy.feature.util

import androidx.databinding.BindingAdapter
import com.evrencoskun.tableview.TableView
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.data.table.ColumnHeader
import com.sachinreddy.feature.data.table.RowHeader
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.table.ui.view.ColumnView
import kotlinx.android.synthetic.main.column_view.view.*

@BindingAdapter("android:cells")
fun setCells(tableView: TableView, cells: List<List<Cell>>) {
    if (tableView.adapter is EditCellAdapter) {
        (tableView.adapter as EditCellAdapter).let {
            it.setCellItems(cells)
            it.cells = cells
        }
    }
}

@BindingAdapter("android:rowHeaders")
fun setRowHeaders(tableView: TableView, rowHeaders: List<RowHeader>) {
    if (tableView.adapter is EditCellAdapter) {
        (tableView.adapter as EditCellAdapter).let {
            it.setRowHeaderItems(rowHeaders)
            it.rowHeaders = rowHeaders
        }
    }
}

@BindingAdapter("android:columnHeaders")
fun setColumnHeaders(tableView: TableView, columnHeaders: List<ColumnHeader>) {
    if (tableView.adapter is EditCellAdapter) {
        (tableView.adapter as EditCellAdapter).let {
            it.setColumnHeaderItems(columnHeaders)
            it.columnHeaders = columnHeaders
        }
    }
}

@BindingAdapter("android:barNumber")
fun setBarNumber(columnView: ColumnView, value: Int) {
    columnView.column_header_barNumber.text = value.toString()
}