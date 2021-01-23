package com.sachinreddy.feature.util.bindingAdapter

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

@BindingAdapter("android:boundText")
fun setText(textView: TextView, value: Int) {
    textView.text = value.toString()
}

@BindingAdapter("android:isScrolling")
fun setScrolling(constraintLayout: ConstraintLayout, isScrolling: Boolean) {
    constraintLayout.visibility = if (isScrolling) View.VISIBLE else View.GONE
}