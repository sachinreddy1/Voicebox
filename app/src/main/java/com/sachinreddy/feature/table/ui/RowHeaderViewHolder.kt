package com.sachinreddy.feature.table.ui

import android.view.View
import androidx.databinding.DataBindingUtil
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.evrencoskun.tableview.data.RowHeader
import com.sachinreddy.feature.databinding.TableViewRowHeaderLayoutBinding
import com.sachinreddy.feature.viewModel.AppViewModel

class RowHeaderViewHolder(
    layout: View,
    private val appViewModel: AppViewModel
) : AbstractViewHolder(layout) {
    private var _rowHeader: RowHeader? = null

    var rowHeader: RowHeader?
        set(value) {
            _rowHeader = value

            binding?.rowHeader = value
            binding?.vm = appViewModel

            binding?.executePendingBindings()
        }
        get() = _rowHeader

    private val binding: TableViewRowHeaderLayoutBinding? = try { DataBindingUtil.bind(itemView) } catch (t: Throwable) { null }
}