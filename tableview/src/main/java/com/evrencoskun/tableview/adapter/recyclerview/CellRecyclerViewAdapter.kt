package com.evrencoskun.tableview.adapter.recyclerview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState
import com.evrencoskun.tableview.adapter.recyclerview.views.CellRecyclerView
import com.evrencoskun.tableview.adapter.recyclerview.views.OverScrollCellRecyclerView
import com.evrencoskun.tableview.data.Cell
import com.evrencoskun.tableview.layoutmanager.ColumnLayoutManager
import com.evrencoskun.tableview.listener.itemclick.CellRecyclerViewItemClickListener
import com.sachinreddy.recyclerview.DiffUtil
import com.sachinreddy.recyclerview.RecyclerView
import java.util.*

class CellRecyclerViewAdapter<C>(
    context: Context,
    items: List<C>?,
    tableView: ITableView
) : AbstractRecyclerViewAdapter<C>(context, items) {
    override var itemList: List<C> = items ?: listOf()
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

    class DiffCallback<C>(private val old: List<C>, private val updated: List<C>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldList = old[oldItemPosition] as MutableList<Cell>
            val newList = updated[newItemPosition] as MutableList<Cell>

            return oldList.zip(newList).all { (x, y) ->
                x.isEqual(y)
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition] == updated[newItemPosition]

        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = updated.size
    }

    var mTableView: ITableView = tableView
    private var mRecycledViewPool: RecyclerView.RecycledViewPool? = null
    private var mRecyclerViewId = 0

    init {
        mRecycledViewPool = RecyclerView.RecycledViewPool()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {

        // Create a RecyclerView as a Row of the CellRecyclerView
        val recyclerView: CellRecyclerView = OverScrollCellRecyclerView(context, mTableView)

        // Use the same view pool
        recyclerView.setRecycledViewPool(mRecycledViewPool)
        if (mTableView.isShowHorizontalSeparators) {
            // Add divider
            recyclerView.addItemDecoration(mTableView.horizontalItemDecoration)
        }

        // To get better performance for fixed size TableView
        recyclerView.setHasFixedSize(mTableView.hasFixedWidth())

        // set touch mHorizontalListener to scroll synchronously
        recyclerView.addOnItemTouchListener(mTableView.horizontalRecyclerViewListener)

        // Add Item click listener for cell views
        if (!mTableView.isAllowClickInsideCell) {
            recyclerView.addOnItemTouchListener(
                CellRecyclerViewItemClickListener(
                    recyclerView,
                    mTableView
                )
            )
        }

        // Set the Column layout manager that helps the fit width of the cell and column header
        // and it also helps to locate the scroll position of the horizontal recyclerView
        // which is row recyclerView
        recyclerView.layoutManager = ColumnLayoutManager(context, mTableView)

        // Create CellRow adapter
        recyclerView.adapter = CellRowRecyclerViewAdapter<Any?>(context, mTableView)

        recyclerView.itemAnimator?.changeDuration = 0
        recyclerView.itemAnimator?.removeDuration = 0
        recyclerView.itemAnimator?.addDuration = 0
        recyclerView.itemAnimator?.moveDuration = 0
        recyclerView.itemAnimator = null

        // This is for testing purpose to find out which recyclerView is displayed.
        recyclerView.id = mRecyclerViewId
        mRecyclerViewId++

        return CellRowViewHolder(recyclerView)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, yPosition: Int) {
        val viewHolder = holder as CellRowViewHolder
        val viewAdapter = viewHolder.recyclerView.adapter as CellRowRecyclerViewAdapter<C>

        // Get the list
        val rowList = itemList[yPosition] as MutableList<C>

        // Set Row position
        viewAdapter.setYPosition(yPosition)

        // Set the list to the adapter
        viewAdapter.itemList = rowList
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder) {
        super.onViewAttachedToWindow(holder)
        val viewHolder = holder as CellRowViewHolder
        val scrollHandler = mTableView.scrollHandler

        // The below code helps to display a new attached recyclerView on exact scrolled position.
        (viewHolder.recyclerView.layoutManager as ColumnLayoutManager)
            .scrollToPositionWithOffset(
                scrollHandler.columnPosition, scrollHandler
                    .columnPositionOffset
            )

        val selectionHandler = mTableView.selectionHandler
        if (selectionHandler.isAnyColumnSelected) {
            val cellViewHolder = viewHolder.recyclerView
                .findViewHolderForAdapterPosition(selectionHandler.selectedColumnPosition) as AbstractViewHolder?
            if (cellViewHolder != null) {
                // Control to ignore selection color
                if (!mTableView.isIgnoreSelectionColors) {
                    cellViewHolder.setBackgroundColor(mTableView.selectedColor)
                }
                cellViewHolder.setSelected(SelectionState.SELECTED)
            }
        } else if (selectionHandler.isRowSelected(holder.getAdapterPosition())) {
            selectionHandler.changeSelectionOfRecyclerView(
                viewHolder.recyclerView,
                SelectionState.SELECTED, mTableView.selectedColor
            )
        }
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder) {
        super.onViewDetachedFromWindow(holder)

        // Clear selection status of the view holder
        mTableView.selectionHandler.changeSelectionOfRecyclerView(
            (holder as CellRowViewHolder).recyclerView,
            SelectionState.UNSELECTED,
            mTableView.unSelectedColor
        )
    }

    override fun onViewRecycled(holder: AbstractViewHolder) {
        super.onViewRecycled(holder)
        val viewHolder = holder as CellRowViewHolder
        // ScrolledX should be cleared at that time. Because we need to prepare each
        // recyclerView
        // at onViewAttachedToWindow process.
        viewHolder.recyclerView.clearScrolledX()
    }

    internal class CellRowViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        val recyclerView: CellRecyclerView = itemView as CellRecyclerView
    }

    fun notifyCellDataSetChanged() {
        val visibleRecyclerViews = mTableView.cellLayoutManager.visibleCellRowRecyclerViews
        if (visibleRecyclerViews!!.isNotEmpty()) {
            for (cellRowRecyclerView in visibleRecyclerViews) {
                cellRowRecyclerView.adapter!!.notifyDataSetChanged()
            }
        } else {
            notifyDataSetChanged()
        }
    }

    /**
     * This method helps to get cell item model that is located on given column position.
     *
     * @param columnPosition
     */
    fun getColumnItems(columnPosition: Int): List<C> {
        val cellItems: MutableList<C> = ArrayList()
        for (i in itemList.indices) {
            val rowList = itemList[i] as List<C>

            if (rowList.size > columnPosition) {
                cellItems.add(rowList[columnPosition])
            }
        }
        return cellItems
    }


    fun removeColumnItems(column: Int) {
        // Firstly, remove columns from visible recyclerViews.
        // To be able provide removing animation, we need to notify just for given column position.
        val visibleRecyclerViews = mTableView.cellLayoutManager.visibleCellRowRecyclerViews

        for (cellRowRecyclerView in visibleRecyclerViews!!) {
            (cellRowRecyclerView.adapter as AbstractRecyclerViewAdapter<*>).deleteItem(column)
        }

        // Lets change the model list silently
        // Create a new list which the column is already removed.
        val cellItems: MutableList<List<C>> = ArrayList()

        for (i in getItems()!!.indices) {
            val rowList: MutableList<C> = itemList[i] as MutableList<C>

            if (rowList.size > column) {
                rowList.removeAt(column)
            }

            cellItems.add(rowList)
        }

        // Change data without notifying. Because we already did for visible recyclerViews.
        itemList = cellItems as MutableList<C>
    }

    fun addColumnItems(
        column: Int,
        cellColumnItems: List<C>
    ) {
        // It should be same size with exist model list.
        if (cellColumnItems.size != getItems()!!.size || cellColumnItems == null) {
            return
        }

        // Firstly, add columns from visible recyclerViews.
        // To be able provide removing animation, we need to notify just for given column position.
        val layoutManager = mTableView.cellLayoutManager
        for (i in layoutManager.findFirstVisibleItemPosition() until layoutManager
            .findLastVisibleItemPosition() + 1) {
            // Get the cell row recyclerView that is located on i position
            val cellRowRecyclerView = layoutManager.findViewByPosition(i) as RecyclerView

            // Add the item using its adapter.
            (cellRowRecyclerView.adapter as AbstractRecyclerViewAdapter<C>).addItem(
                column,
                cellColumnItems[i]
            )
        }

        // Lets change the model list silently
        val cellItems: MutableList<List<C>> = ArrayList()

        for (i in itemList.indices) {
            val rowList: MutableList<C> = itemList[i] as MutableList<C>

            if (rowList.size > column) {
                rowList.add(column, cellColumnItems[i])
            }

            cellItems.add(rowList)
        }

        // Change data without notifying. Because we already did for visible recyclerViews.
        itemList = cellItems as MutableList<C>
    }
}