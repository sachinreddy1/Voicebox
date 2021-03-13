package com.evrencoskun.tableview.adapter.recyclerview

import android.content.Context
import android.view.ViewGroup
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.adapter.ITableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.evrencoskun.tableview.sort.RowHeaderSortHelper
import com.sachinreddy.recyclerview.DiffUtil

class RowHeaderRecyclerViewAdapter<RH>(
    context: Context,
    items: List<RH>?,
    tableAdapter: ITableAdapter<*, *, RH, *>
) : AbstractRecyclerViewAdapter<RH>(context, items) {
    override var itemList: MutableList<RH> = items?.toMutableList() ?: mutableListOf()
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

    class DiffCallback<RH>(private val old: MutableList<RH>, private val updated: MutableList<RH>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition] == updated[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition] == updated[newItemPosition]

        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = updated.size
    }

    private val mTableAdapter: ITableAdapter<*, *, RH, *> = tableAdapter
    private val mTableView: ITableView = tableAdapter.tableView
    lateinit var mRowHeaderSortHelper: RowHeaderSortHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        return mTableAdapter.onCreateRowHeaderViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        mTableAdapter.onBindRowHeaderViewHolder(holder, itemList[position], position)
    }

    override fun getItemViewType(position: Int): Int {
        return mTableAdapter.getRowHeaderItemViewType(position)
    }

    override fun onViewAttachedToWindow(viewHolder: AbstractViewHolder) {
        super.onViewAttachedToWindow(viewHolder)
        val selectionState =
            mTableView.selectionHandler.getRowSelectionState(viewHolder.adapterPosition)

        // Control to ignore selection color
        if (!mTableView.isIgnoreSelectionColors) {
            // Change background color of the view considering it's selected state
            mTableView.selectionHandler.changeRowBackgroundColorBySelectionStatus(
                viewHolder,
                selectionState
            )
        }

        // Change selection status
        viewHolder.setSelected(selectionState)
    }

    fun getRowHeaderSortHelper(): RowHeaderSortHelper {
        if (mRowHeaderSortHelper == null) {
            // It helps to store sorting state of row headers
            mRowHeaderSortHelper = RowHeaderSortHelper()
        }
        return mRowHeaderSortHelper
    }
}