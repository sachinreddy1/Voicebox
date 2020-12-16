package com.sachinreddy.feature.util.bindingAdapter

import androidx.databinding.BindingAdapter
import com.sachinreddy.feature.table.ui.view.OperationView
import com.sachinreddy.feature.viewModel.AppViewModel

@BindingAdapter("android:vm")
fun setVM(operationView: OperationView, vm: AppViewModel) {
    operationView.binding.vm = vm
}