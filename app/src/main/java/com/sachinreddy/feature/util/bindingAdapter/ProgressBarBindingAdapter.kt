package com.sachinreddy.feature.util.bindingAdapter

import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

@BindingAdapter("android:progressValue")
fun setProgressValue(progressBar: ProgressBar, value: Float) {
    progressBar.progress = (value * 100).toInt()
}