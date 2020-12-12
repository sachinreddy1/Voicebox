package com.sachinreddy.feature.table.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.sachinreddy.feature.R

class ColumnView (context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {
    init {
        inflate(context, R.layout.column_view, this)

        val columnHeaderBackground: ImageView = findViewById(R.id.column_header_background)
        val columnHeaderBarNumber: TextView = findViewById(R.id.column_header_barNumber)

        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.ColumnView)
        columnHeaderBackground.backgroundTintList = attributes.getColorStateList(R.styleable.ColumnView_backgroundColor)
        columnHeaderBarNumber.text = attributes.getInteger(R.styleable.ColumnView_barNumber, 1).toString()
        attributes.recycle()
    }
}