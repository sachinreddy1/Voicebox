package com.sachinreddy.feature.table.listener

import android.content.Context
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.Rect
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sachinreddy.feature.R
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import kotlin.math.roundToInt

class CellDragListener(
    private val context: Context,
    private val editCellAdapter: EditCellAdapter
) : View.OnDragListener {

    override fun onDrag(v: View, event: DragEvent): Boolean {
        val containerView: ConstraintLayout = v as ConstraintLayout
        val draggedView: ConstraintLayout = event.localState as ConstraintLayout
        return when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_STARTED")
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_ENTERED")
                val highlightColor = context.getColor(R.color.colorPrimary)
                containerView.background.setColorFilter(highlightColor, PorterDuff.Mode.SRC_IN)
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_EXITED")
                containerView.background.clearColorFilter()
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                Log.d(TAG, "onDrag: ACTION_DROP")
                containerView.invalidate()
                draggedView.post(Runnable { draggedView.visibility = View.GONE })
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_ENDED")
                v.visibility = View.VISIBLE
                draggedView.post(Runnable { draggedView.visibility = View.VISIBLE })
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

    companion object {
        private const val TAG = "TrashDragListener"
    }
}