package com.sachinreddy.feature.util.bindingAdapter

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.evrencoskun.tableview.data.Cell
import com.sachinreddy.feature.table.listener.ScrollingListener
import com.sachinreddy.feature.table.ui.view.OperationView
import com.sachinreddy.feature.viewModel.AppViewModel

@BindingAdapter("android:vm")
fun setVM(operationView: OperationView, vm: AppViewModel) {
    operationView.binding.vm = vm
}

@BindingAdapter("android:draggedCell")
fun setDragging(constraintLayout: ConstraintLayout, cell: Cell?) {
    constraintLayout.visibility = if (cell != null) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["android:vm", "android:right"], requireAll = true)
fun bindOnScrollingListener(
    constraintLayout: ConstraintLayout,
    appViewModel: AppViewModel,
    right: Boolean
) {
    constraintLayout.setOnDragListener(ScrollingListener(appViewModel, right))
}