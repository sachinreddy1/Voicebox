package com.evrencoskun.tableview.adapter.recyclerview

import android.content.Context
import android.view.ViewGroup
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.adapter.ITableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.evrencoskun.tableview.sort.ColumnSortHelper
import com.sachinreddy.recyclerview.DiffUtil

class ColumnHeaderRecyclerViewAdapter<CH>(
    context: Context,
    items: List<CH>?,
    tableAdapter: ITableAdapter<*, CH, *, *>
) : AbstractRecyclerViewAdapter<CH>(context, items) {
    override var itemList: List<CH> = items ?: listOf()
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

    class DiffCallback<CH>(private val old: List<CH>, private val updated: List<CH>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition] == updated[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition] == updated[newItemPosition]

        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = updated.size
    }

    private val mTableAdapter: ITableAdapter<*, CH, *, *> = tableAdapter
    private val mTableView: ITableView = tableAdapter.tableView
    lateinit var mColumnSortHelper: ColumnSortHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        return mTableAdapter.onCreateColumnHeaderViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        return mTableAdapter.onBindColumnHeaderViewHolder(holder, itemList[position], position)
    }

    override fun getItemViewType(position: Int): Int {
        return mTableAdapter.getColumnHeaderItemViewType(position)
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

        // Control whether the TableView is sortable or not.
        if (mTableView.isSortable) {
            if (viewHolder is AbstractSorterViewHolder) {
                // Get its sorting state
                val state = getColumnSortHelper().getSortingStatus(viewHolder.getAdapterPosition())
                // Fire onSortingStatusChanged
                viewHolder.onSortingStatusChanged(state)
            }
        }
    }

    fun getColumnSortHelper(): ColumnSortHelper {
        if (mColumnSortHelper == null) {
            // It helps to store sorting state of column headers
            mColumnSortHelper = ColumnSortHelper(mTableView.columnHeaderLayoutManager)
        }
        return mColumnSortHelper
    }
}