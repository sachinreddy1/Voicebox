package com.sachinreddy.feature.util.bindingAdapter

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.evrencoskun.tableview.TableView
import com.sachinreddy.feature.R
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.table.listener.EditCellListener
import com.sachinreddy.feature.table.ui.view.SmallFAB
import com.sachinreddy.feature.viewModel.AppViewModel
import com.sachinreddy.recordbutton.OnRecordListener
import com.sachinreddy.recordbutton.RecordButton
import kotlinx.android.synthetic.main.small_fab.view.*
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

@BindingAdapter(
    value = ["android:isPlaying", "android:vm"],
    requireAll = true
)
fun setIsPlaying(constraintLayout: ConstraintLayout, isPlaying: Boolean, vm: AppViewModel) {
    if (isPlaying) {
        constraintLayout.fab_icon.setImageResource(R.drawable.ic_pause)
        vm.startPlaying()
    } else {
        constraintLayout.fab_icon.setImageResource(R.drawable.ic_play)
        vm.stopPlaying()
    }
}

@BindingAdapter("android:isFabEnabled")
fun setIsFabEnabled(smallFAB: SmallFAB, isFabEnabled: Boolean) {
    smallFAB.setFabEnabled(isFabEnabled)
}

@BindingAdapter("android:vm")
fun setRecordButtonVm(recordButton: RecordButton, vm: AppViewModel) {
    vm.apply {
        recordButton.setRecordListener(object : OnRecordListener {
            override fun onRecord() {
                if (!isRecording) {
                    startRecording()
                }
            }

            override fun onRecordCancel() {
                if (isRecording) {
                    stopRecording()
                }
            }

            override fun onRecordFinish() {
                if (isRecording) {
                    stopRecording()
                }
            }
        })
    }
}

@BindingAdapter("app:maxMillis")
fun setMaxMillis(recordButton: RecordButton, maxMillis: Int) {
    recordButton.maxMilisecond = maxMillis
}

@BindingAdapter("app:adapter")
fun setEditCellAdapter(tableView: TableView, adapter: EditCellAdapter) {
    tableView.adapter = adapter
}

@BindingAdapter("app:tableViewListener")
fun setEditCellListener(tableView: TableView, tableViewListener: EditCellListener) {
    tableView.tableViewListener = tableViewListener
}