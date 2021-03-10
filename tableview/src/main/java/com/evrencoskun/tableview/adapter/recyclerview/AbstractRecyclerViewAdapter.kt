package com.evrencoskun.tableview.adapter.recyclerview

import android.content.Context
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.recyclerview.DiffUtil
import com.sachinreddy.recyclerview.RecyclerView

abstract class AbstractRecyclerViewAdapter<T>(
    val context: Context,
    items: List<T>?
) : RecyclerView.Adapter<AbstractViewHolder>() {
    var itemList: MutableList<T> = mutableListOf()
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

    class DiffCallback<T>(private val old: MutableList<T>, private val updated: MutableList<T>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition] == updated[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition] == updated[newItemPosition]

        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = updated.size
    }

    init {
        setItems(items)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @NonNull
    open fun getItems(): List<T>? {
        return itemList
    }

    open fun setItems(@NonNull items: List<T>?) {
        items?.let {
            itemList = it.toMutableList()
        }
    }

    @Nullable
    open fun getItem(position: Int): T? {
        return if (itemList.isEmpty() || position < 0 || position >= itemList.size) {
            null
        } else itemList[position]
    }

    open fun deleteItem(position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            itemList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    open fun deleteItemRange(positionStart: Int, itemCount: Int) {
        for (i in positionStart + itemCount - 1 downTo positionStart) {
            if (i != RecyclerView.NO_POSITION) {
                itemList.removeAt(i)
            }
        }
        notifyItemRangeRemoved(positionStart, itemCount)
    }

    open fun addItem(position: Int, item: T) {
        if (position != RecyclerView.NO_POSITION && item != null) {
            itemList.add(position, item)
            notifyItemInserted(position)
        }
    }

    open fun addItemRange(positionStart: Int, items: List<T>?) {
        if (items != null) {
            for (i in items.indices) {
                itemList.add(i + positionStart, items[i])
            }
            notifyItemRangeInserted(positionStart, items.size)
        }
    }

    open fun changeItem(position: Int, item: T?) {
        if (position != RecyclerView.NO_POSITION && item != null) {
            itemList.set(position, item)
            notifyItemChanged(position)
        }
    }

    open fun changeItemRange(positionStart: Int, items: List<T>?) {
        if (items != null && itemList.size > positionStart + items.size) {
            for (i in items.indices) {
                itemList.set(i + positionStart, items[i])
            }
            notifyItemRangeChanged(positionStart, items.size)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }
}