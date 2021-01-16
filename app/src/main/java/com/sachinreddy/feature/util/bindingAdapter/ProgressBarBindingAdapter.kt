package com.sachinreddy.feature.util.bindingAdapter

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

@BindingAdapter("android:horizontalBias")
fun setHorizontalBias(constraintLayout: ConstraintLayout, value: Float) {
    val params = constraintLayout.layoutParams as ConstraintLayout.LayoutParams
    params.horizontalBias = value
    constraintLayout.layoutParams = params
}