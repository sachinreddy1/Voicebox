package com.sachinreddy.feature.util.bindingAdapter

import androidx.databinding.BindingAdapter
import com.evrencoskun.tableview.TableView
import com.sachinreddy.feature.data.table.Timeline
import com.sachinreddy.feature.table.adapter.EditCellAdapter

@BindingAdapter("android:timelineHeaders")
fun setTimelineHeaders(tableView: TableView, timelineHeaders: List<Timeline>) {
    if (tableView.adapter is EditCellAdapter) {
        (tableView.adapter as EditCellAdapter).let {
            it.setTimelineItems(timelineHeaders)
        }
    }
}