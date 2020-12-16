package com.sachinreddy.feature.table.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.sachinreddy.feature.R
import com.sachinreddy.feature.databinding.OperationViewBinding

class OperationView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {
    var binding: OperationViewBinding =
        OperationViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val operationViewContainer: ConstraintLayout = findViewById(R.id.operation_view_container)
        val operationAddButton: ImageButton = findViewById(R.id.operation_add_button)
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.OperationView)
        operationViewContainer.backgroundTintList =
            attributes.getColorStateList(R.styleable.OperationView_operationBackgroundColor)
        operationAddButton.backgroundTintList =
            attributes.getColorStateList(R.styleable.OperationView_buttonBackgroundColor)
        attributes.recycle()
    }
}