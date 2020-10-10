package com.sachinreddy.feature.table.listener

import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.evrencoskun.tableview.ITableView

class CellDragListener(private val tableView: ITableView) : View.OnDragListener {
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
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                val translatedX = event.x - tableView.columnHeaderRecyclerView.scrolledX
                val threshold = 50

                if (translatedX < threshold) {
                    tableView.columnHeaderRecyclerView.scrollBy(30, 0)
                    tableView.cellLayoutManager.visibleCellRowRecyclerViews?.get(0)?.scrollBy(30, 0)
                }
                false
            }
            else -> true
        }
    }

    companion object {
        private const val TAG = "TrashDragListener"
    }
}