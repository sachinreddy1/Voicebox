package com.sachinreddy.feature.table.ui

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.databinding.TableViewCellLayoutBinding
import com.sachinreddy.feature.table.listener.SelectionListener
import com.sachinreddy.feature.table.listener.TranslationListener
import com.sachinreddy.feature.viewModel.AppViewModel

class CellViewHolder(
    itemView: View,
    val context: Context,
    private val appViewModel: AppViewModel
) : AbstractViewHolder(itemView) {
    private var _cell: Cell? = null

    var cell: Cell?
        set(value) {
            _cell = value!!

            binding?.cell = value
            binding?.vm = appViewModel

            binding?.translationListener = TranslationListener(
                context,
                value,
                appViewModel
            )

            binding?.selectionListener = SelectionListener(
                value,
                appViewModel
            )

            binding?.executePendingBindings()
        }
        get() = _cell

    private val binding: TableViewCellLayoutBinding? = try { DataBindingUtil.bind(itemView) } catch (t: Throwable) { null }
}