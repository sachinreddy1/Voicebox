package com.sachinreddy.feature.util.bindingAdapter

import androidx.databinding.BindingAdapter
import com.evrencoskun.tableview.TableView
import com.sachinreddy.feature.data.table.ColumnHeader
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.table.ui.view.ColumnView
import kotlinx.android.synthetic.main.column_view.view.*

@BindingAdapter("android:columnHeaders")
fun setColumnHeaders(tableView: TableView, columnHeaders: List<ColumnHeader>) {
    if (tableView.adapter is EditCellAdapter) {
        (tableView.adapter as EditCellAdapter).let {
            it.setColumnHeaderItems(columnHeaders)
        }
    }
}

@BindingAdapter("android:barNumber")
fun setBarNumber(columnView: ColumnView, value: Int) {
    columnView.column_header_barNumber.text = value.toString()
}