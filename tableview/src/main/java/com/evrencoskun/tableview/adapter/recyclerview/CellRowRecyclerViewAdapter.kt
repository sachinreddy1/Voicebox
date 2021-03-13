package com.evrencoskun.tableview.adapter.recyclerview

import android.content.Context
import android.view.ViewGroup
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState
import com.evrencoskun.tableview.data.Cell
import com.sachinreddy.recyclerview.DiffUtil

class CellRowRecyclerViewAdapter<C>(
    context: Context,
    tableView: ITableView
) : AbstractRecyclerViewAdapter<C>(context, null) {

    override var itemList: MutableList<C> = mutableListOf()
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

    class DiffCallback<C>(private val old: MutableList<C>, private val updated: MutableList<C>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            (old[oldItemPosition] as Cell) == (updated[newItemPosition] as Cell)

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            (old[oldItemPosition] as Cell).isEqual(updated[newItemPosition] as Cell)

        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = updated.size
    }

    private var mYPosition = 0
    private val mTableView: ITableView = tableView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        return mTableView.adapter!!.onCreateCellViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, xPosition: Int) {
        mTableView.adapter!!.onBindCellViewHolder(
            holder,
            itemList[xPosition],
            xPosition,
            mYPosition
        )
    }

    fun getYPosition(): Int {
        return mYPosition
    }

    fun setYPosition(rowPosition: Int) {
        mYPosition = rowPosition
    }

    override fun getItemViewType(position: Int): Int {
        return mTableView.adapter!!.getCellItemViewType(position)
    }

    override fun onViewAttachedToWindow(viewHolder: AbstractViewHolder) {
        super.onViewAttachedToWindow(viewHolder)
        val selectionState = mTableView.selectionHandler
            .getCellSelectionState(viewHolder.adapterPosition, mYPosition)

        // Control to ignore selection color
        if (!mTableView.isIgnoreSelectionColors) {

            // Change the background color of the view considering selected row/cell position.
            if (selectionState == SelectionState.SELECTED) {
                viewHolder.setBackgroundColor(mTableView.selectedColor)
            } else {
                viewHolder.setBackgroundColor(mTableView.unSelectedColor)
            }
        }

        // Change selection status
        viewHolder.setSelected(selectionState)
    }

    override fun onFailedToRecycleView(holder: AbstractViewHolder): Boolean {
        return holder.onFailedToRecycleView()
    }

    override fun onViewRecycled(holder: AbstractViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}