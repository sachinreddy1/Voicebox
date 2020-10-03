package com.sachinreddy.feature.table.holder

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R

class RowHeaderViewHolder(
    layout: View
) : AbstractViewHolder(layout) {
    val row_header_imageView: ImageView = layout.findViewById(R.id.row_header_imageView)
    val row_header_button_container: ConstraintLayout = layout.findViewById(R.id.row_header_button_container)
    val row_header_button: ImageButton = layout.findViewById(R.id.row_header_button)
}