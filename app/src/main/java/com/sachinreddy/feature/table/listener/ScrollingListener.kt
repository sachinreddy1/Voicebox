package com.sachinreddy.feature.table.listener

import android.content.Context
import android.view.DragEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sachinreddy.feature.R
import com.sachinreddy.feature.viewModel.AppViewModel

class ScrollingListener(
    private val context: Context,
    private val appViewModel: AppViewModel
) : View.OnDragListener {
    override fun onDrag(v: View, event: DragEvent): Boolean {
        val containerView: ConstraintLayout = v as ConstraintLayout
        return when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DRAG_ENTERED -> {
                val highlightColor = context.getColor(R.color.whitesmoke)
                containerView.setBackgroundColor(highlightColor)
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                val transparentColor = context.getColor(R.color.transparent)
                containerView.setBackgroundColor(transparentColor)
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                val transparentColor = context.getColor(R.color.transparent)
                containerView.setBackgroundColor(transparentColor)
                containerView.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
//                editCellAdapter.stopScrollThread()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                false
            }
            else -> true
        }
    }
}