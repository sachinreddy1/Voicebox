package com.sachinreddy.feature.table.listener

import android.view.DragEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.viewModel.AppViewModel

class SelectionListener(
    private var cell: Cell,
    private val appViewModel: AppViewModel
) : View.OnDragListener {
    var cells: List<List<Cell>> = appViewModel.cells.value!!

    override fun onDrag(v: View, event: DragEvent): Boolean {
        val containerView: ConstraintLayout = v as ConstraintLayout
        return when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DRAG_ENTERED -> {
                cells = appViewModel.selectCells(cell)
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                containerView.background.clearColorFilter()
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                appViewModel.cells.postValue(cells)
                containerView.background.clearColorFilter()
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                appViewModel.cells.postValue(cells)
                containerView.background.clearColorFilter()
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> false
            else -> true
        }
    }
}