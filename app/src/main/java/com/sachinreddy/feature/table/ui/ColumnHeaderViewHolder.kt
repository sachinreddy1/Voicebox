package com.sachinreddy.feature.table.ui

import android.view.View
import androidx.databinding.DataBindingUtil
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.evrencoskun.tableview.data.ColumnHeader
import com.sachinreddy.feature.databinding.TableViewColumnHeaderLayoutBinding
import com.sachinreddy.feature.viewModel.AppViewModel

class ColumnHeaderViewHolder(
    layout: View,
    private val appViewModel: AppViewModel
) : AbstractViewHolder(layout) {
    private var _columnHeader: ColumnHeader? = null

    var columnHeader: ColumnHeader?
        set(value) {
            _columnHeader = value

            binding?.columnHeader = value
            binding?.vm = appViewModel

            binding?.executePendingBindings()
        }
        get() = _columnHeader

    private val binding: TableViewColumnHeaderLayoutBinding? = try { DataBindingUtil.bind(itemView) } catch (t: Throwable) { null }
}