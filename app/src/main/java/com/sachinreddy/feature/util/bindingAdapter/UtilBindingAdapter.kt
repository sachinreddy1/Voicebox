package com.sachinreddy.feature.util.bindingAdapter

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import java.util.concurrent.TimeUnit

@BindingAdapter(
    value = ["android:time", "android:maxTime", "android:bpm", "android:numberBars"],
    requireAll = true
)
fun setText(
    textView: TextView,
    time: Int,
    maxTime: Int,
    bpm: Int,
    numberBars: Int
) {
    val barNumber = time * (numberBars.toFloat() / maxTime)
    val beatNumber = 4 * barNumber
    val timeMS = beatNumber * (60000 / bpm)

    timeMS.toLong().let { millis ->
        textView.text = String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    millis
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    millis
                )
            ),
            TimeUnit.MILLISECONDS.toMillis(millis) - TimeUnit.SECONDS.toMillis(
                TimeUnit.MILLISECONDS.toSeconds(
                    millis
                )
            )
        )
    }
}

@BindingAdapter("android:isScrolling")
fun setScrolling(constraintLayout: ConstraintLayout, isScrolling: Boolean) {
    constraintLayout.visibility = if (isScrolling) View.VISIBLE else View.GONE
}