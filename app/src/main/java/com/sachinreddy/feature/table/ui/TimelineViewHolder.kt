package com.sachinreddy.feature.table.ui

import android.view.View
import androidx.databinding.DataBindingUtil
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.evrencoskun.tableview.data.Timeline
import com.sachinreddy.feature.databinding.TableViewColumnHeaderLayoutBinding

class TimelineViewHolder(
    layout: View
) : AbstractViewHolder(layout) {
    private var _timeline: Timeline? = null

    var timeline: Timeline?
        set(value) {
            _timeline = value
            binding?.executePendingBindings()
        }
        get() = _timeline

    private val binding: TableViewColumnHeaderLayoutBinding? = try {
        DataBindingUtil.bind(itemView)
    } catch (t: Throwable) {
        null
    }
}