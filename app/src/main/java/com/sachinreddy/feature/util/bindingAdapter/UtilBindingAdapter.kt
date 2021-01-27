package com.sachinreddy.feature.util.bindingAdapter

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.sachinreddy.feature.viewModel.AppViewModel
import java.util.concurrent.TimeUnit

@BindingAdapter(value = ["android:boundText", "android:vm"], requireAll = true)
fun setText(
    textView: TextView,
    boundText: Int,
    vm: AppViewModel
) {
    vm.tableView.timelineRecyclerView.apply {
        mMaxTime.value?.toFloat()?.let { _maxTime ->
            vm.bpm.value?.toFloat()?.let { bpm ->
                val barNumber = boundText * (vm.numberBars.toFloat() / _maxTime)
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
                            TimeUnit.MILLISECONDS.toMinutes(millis)
                        ),
                        TimeUnit.MILLISECONDS.toMillis(millis) - TimeUnit.SECONDS.toMillis(
                            TimeUnit.MILLISECONDS.toSeconds(
                                millis
                            )
                        )
                    )
                }
            }
        }
    }
}

@BindingAdapter("android:isScrolling")
fun setScrolling(constraintLayout: ConstraintLayout, isScrolling: Boolean) {
    constraintLayout.visibility = if (isScrolling) View.VISIBLE else View.GONE
}