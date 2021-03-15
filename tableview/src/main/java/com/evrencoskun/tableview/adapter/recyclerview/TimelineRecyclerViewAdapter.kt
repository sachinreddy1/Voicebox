package com.evrencoskun.tableview.adapter.recyclerview

import android.content.Context
import android.view.ViewGroup
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.adapter.ITableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.recyclerview.DiffUtil

class TimelineRecyclerViewAdapter<T>(
    context: Context,
    items: List<T>?,
    tableAdapter: ITableAdapter<T, *, *, *>
) : AbstractRecyclerViewAdapter<T>(context, items) {
    override var itemList: List<T> = items ?: listOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(
                DiffCallback(
                    itemList,
                    value
                ), true
            )
            field = value
            diff.dispatchUpdatesTo(this)
        }

    class DiffCallback<T>(private val old: List<T>, private val updated: List<T>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition] == updated[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition] == updated[newItemPosition]

        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = updated.size
    }

    private val mTableAdapter: ITableAdapter<T, *, *, *> = tableAdapter
    private val mTableView: ITableView = tableAdapter.tableView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        return mTableAdapter.onCreateTimelineViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        mTableAdapter.onBindTimelineViewHolder(holder, itemList[position], position)
    }

    override fun getItemViewType(position: Int): Int {
        return mTableAdapter.getTimelineItemViewType(position)
    }

    override fun onViewAttachedToWindow(viewHolder: AbstractViewHolder) {
        super.onViewAttachedToWindow(viewHolder)
        val selectionState =
            mTableView.selectionHandler.getColumnSelectionState(viewHolder.adapterPosition)

        // Control to ignore selection color
        if (!mTableView.isIgnoreSelectionColors) {
            // Change background color of the view considering it's selected state
            mTableView.selectionHandler.changeColumnBackgroundColorBySelectionStatus(
                viewHolder,
                selectionState
            )
        }

        // Change selection status
        viewHolder.setSelected(selectionState)
    }
}