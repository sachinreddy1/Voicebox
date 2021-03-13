package com.sachinreddy.feature.util.bindingAdapter

import androidx.databinding.BindingAdapter
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.data.RowHeader
import com.sachinreddy.feature.table.adapter.EditCellAdapter

@BindingAdapter("android:rowHeaders")
fun setRowHeaders(tableView: TableView, rowHeaders: List<RowHeader>) {
    if (tableView.adapter is EditCellAdapter) {
        (tableView.adapter as EditCellAdapter).let {
            it.setRowHeaderItems(rowHeaders)
        }
    }
}