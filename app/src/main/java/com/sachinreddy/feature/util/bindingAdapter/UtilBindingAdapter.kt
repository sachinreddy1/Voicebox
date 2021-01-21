package com.sachinreddy.feature.util.bindingAdapter

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:boundText")
fun setText(textView: TextView, value: Int) {
    textView.text = value.toString()
}