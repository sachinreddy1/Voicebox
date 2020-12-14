package com.sachinreddy.feature.table.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.sachinreddy.feature.R
import com.sachinreddy.feature.databinding.CellViewBinding

class CellView (context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {
    var binding: CellViewBinding = CellViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val cellItem: CardView = findViewById(R.id.cell_item)
        val actionButton: ImageButton = findViewById(R.id.action_button)
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.CellView)
        cellItem.backgroundTintList = attributes.getColorStateList(R.styleable.CellView_cellColor)
        actionButton.backgroundTintList =
            attributes.getColorStateList(R.styleable.CellView_buttonColor)
        attributes.recycle()
    }
}