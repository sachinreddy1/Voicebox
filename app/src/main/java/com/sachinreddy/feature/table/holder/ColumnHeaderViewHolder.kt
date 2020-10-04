package com.sachinreddy.feature.table.holder

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.data.table.TimelineHeader

class ColumnHeaderViewHolder(
    layout: View
) : AbstractViewHolder(layout) {
    val column_header_barNumber: TextView = layout.findViewById(R.id.column_header_barNumber)

    fun bind(columnHeader: TimelineHeader) {
        column_header_barNumber.text = columnHeader.data.toString()
    }
}