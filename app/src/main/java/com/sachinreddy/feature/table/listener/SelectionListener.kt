package com.sachinreddy.feature.table.listener

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.view.DragEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.viewModel.AppViewModel
import kotlin.math.roundToInt

class SelectionListener(
    private val context: Context,
    private val editCellAdapter: EditCellAdapter,
    private val appViewModel: AppViewModel,
    private var cell: Cell
) : View.OnDragListener {

    override fun onDrag(v: View, event: DragEvent): Boolean {
        val containerView: ConstraintLayout = v as ConstraintLayout
        return when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DRAG_ENTERED -> {
                var columnRange = listOf<Int>()
                var rowRange = listOf<Int>()

                appViewModel.startingCell?.let {
                    columnRange = if (it.columnPosition < cell.columnPosition)
                        (it.columnPosition..cell.columnPosition).toList()
                    else
                        (it.columnPosition downTo cell.columnPosition).toList()

                    rowRange = if (it.rowPosition < cell.rowPosition)
                        (it.rowPosition..cell.rowPosition).toList()
                    else
                        (it.rowPosition downTo cell.rowPosition).toList()
                }

                editCellAdapter.clearSelectedCells()
                appViewModel.startingCell?.let {
                    for (i in columnRange) {
                        for (j in rowRange) {
                            val cell = editCellAdapter.getCellItem(i, j)
                            cell?.isSelected = true
                        }
                    }
                }
                editCellAdapter.notifyDataSetChanged()
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                containerView.background.clearColorFilter()
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                containerView.background.clearColorFilter()
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                editCellAdapter.apply {
                    stopScrollThread()
                    if (!isScrolling) {
                        notifyDataSetChanged()
                    }
                }
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                val touchPosition = getTouchPositionFromDragEvent(v, event)
                editCellAdapter.xPosition = touchPosition.x
                false
            }
            else -> true
        }
    }

    private fun getTouchPositionFromDragEvent(item: View, event: DragEvent): Point {
        val rItem = Rect()
        (item.parent as View).getGlobalVisibleRect(rItem)

        val entireWidth = (item.parent.parent as View).width
        val hiddenWidth = item.width - (rItem.right - rItem.left)

        // If left most item
        return if (rItem.left < entireWidth/2) {
            Point(
                rItem.left + (event.x.roundToInt() - hiddenWidth),
                rItem.top + event.y.roundToInt()
            )
        } else {
            Point(
                rItem.left + event.x.roundToInt(),
                rItem.top + event.y.roundToInt()
            )
        }
    }
}