package com.sachinreddy.feature.table.listener

import android.content.Context
import android.graphics.PorterDuff
import android.view.DragEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.viewModel.AppViewModel

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
                val highlightColor = context.getColor(R.color.recordButtonColor)
                containerView.background.setColorFilter(highlightColor, PorterDuff.Mode.SRC_IN)
                containerView.invalidate()
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
                false
            }
            else -> true
        }
    }
}