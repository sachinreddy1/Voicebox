package com.sachinreddy.feature.table.listener

import android.view.DragEvent
import android.view.View
import com.sachinreddy.feature.viewModel.AppViewModel

class ScrollingListener(
    private val appViewModel: AppViewModel,
    private val right: Boolean
) : View.OnDragListener {
    override fun onDrag(v: View, event: DragEvent): Boolean {
        return when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DRAG_ENTERED -> {
                appViewModel.startScrolling(right)
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                appViewModel.stopScrolling()
                true
            }
            DragEvent.ACTION_DROP -> {
                appViewModel.draggedCell.value?.let { appViewModel.dropCell(it) }
                appViewModel.stopScrolling()
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                appViewModel.stopScrolling()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                false
            }
            else -> true
        }
    }
}