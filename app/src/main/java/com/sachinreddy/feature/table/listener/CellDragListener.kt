package com.sachinreddy.feature.table.listener

import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class CellDragListener : View.OnDragListener {
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
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                Log.d(
                    TAG,
                    "onDrag: ACTION_DRAG_EXITED"
                )
                true
            }
            DragEvent.ACTION_DROP -> {
                Log.d(TAG, "onDrag: ACTION_DROP")
                hit = true
                draggedView.post(Runnable { draggedView.setVisibility(View.GONE) })
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_ENDED")
                v.visibility = View.VISIBLE
                if (!hit) {
                    draggedView.post(Runnable { draggedView.setVisibility(View.VISIBLE) })
                }
                true
            }
            else -> true
        }
    }

    companion object {
        private const val TAG = "TrashDragListener"
    }
}