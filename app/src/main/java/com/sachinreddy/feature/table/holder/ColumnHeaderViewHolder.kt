package com.sachinreddy.feature.table.holder

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R

class ColumnHeaderViewHolder(
    layout: View
) : AbstractViewHolder(layout) {
    val column_header_barNumber: TextView = layout.findViewById(R.id.column_header_barNumber)
}