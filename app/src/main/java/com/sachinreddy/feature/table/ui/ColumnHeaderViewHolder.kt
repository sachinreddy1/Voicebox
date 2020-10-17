package com.sachinreddy.feature.table.ui

import android.view.View
import android.widget.TextView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.TimelineHeader

class ColumnHeaderViewHolder(
    layout: View
) : AbstractViewHolder(layout) {
    val column_header_barNumber: TextView = layout.findViewById(R.id.column_header_barNumber)

    fun bind(columnHeader: TimelineHeader) {
        column_header_barNumber.text = columnHeader.data.toString()
    }
}