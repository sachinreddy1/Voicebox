package com.sachinreddy.feature.table.listener

import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.Rect
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.evrencoskun.tableview.ITableView
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import kotlin.math.roundToInt

class CellDragListener(private val tableView: ITableView, private val editCellAdapter: EditCellAdapter) : View.OnDragListener {
    private var hit = false
    override fun onDrag(v: View, event: DragEvent): Boolean {
        val containerView: ConstraintLayout = v as ConstraintLayout
        val draggedView: ConstraintLayout = event.localState as ConstraintLayout
        return when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                Log.d(
                    TAG,
                    "onDrag: ACTION_DRAG_STARTED"
                )
                hit = false
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                Log.d(
                    TAG,
                    "onDrag: ACTION_DRAG_ENTERED"
                )
                containerView.background.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN)
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                Log.d(
                    TAG,
                    "onDrag: ACTION_DRAG_EXITED"
                )
                containerView.background.clearColorFilter()
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                containerView.background.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
                containerView.invalidate()

                Log.d(TAG, "onDrag: ACTION_DROP")
                hit = true
                draggedView.post(Runnable { draggedView.visibility = View.GONE })
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_ENDED")
                v.visibility = View.VISIBLE
                if (!hit) {
                    draggedView.post(Runnable { draggedView.visibility = View.VISIBLE })
                }
                editCellAdapter.stopScrollThread()
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

    fun getTouchPositionFromDragEvent(item: View, event: DragEvent): Point {
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

    companion object {
        private const val TAG = "TrashDragListener"
    }
}