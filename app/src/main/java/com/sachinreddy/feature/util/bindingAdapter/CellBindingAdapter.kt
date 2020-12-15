package com.sachinreddy.feature.util.bindingAdapter

import android.graphics.Color
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.evrencoskun.tableview.TableView
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.table.listener.TranslationListener
import com.sachinreddy.feature.table.ui.view.CellView
import kotlinx.android.synthetic.main.cell_view.view.*

@BindingAdapter("android:cells")
fun setCells(tableView: TableView, cells: List<List<Cell>>) {
    if (tableView.adapter is EditCellAdapter) {
        (tableView.adapter as EditCellAdapter).let {
            it.setCellItems(cells)
            it.cells = cells
        }
    }
}

@BindingAdapter("android:data")
fun setData(cellView: CellView, data: List<ShortArray>) {
    cellView.visibility = if (data.isEmpty()) View.GONE else View.VISIBLE
}

@BindingAdapter("android:isSelected")
fun setSelected(constraintLayout: ConstraintLayout, isSelected: Boolean) {
    val backgroundColor = if (isSelected) "#616161" else "#2C2C2C"
    constraintLayout.setBackgroundColor(Color.parseColor(backgroundColor))
}

@BindingAdapter("android:isPlaying")
fun setIsPlaying(cellView: CellView, isPlaying: Boolean) {
    cellView.action_button.setImageResource(
        if (isPlaying) R.drawable.ic_stop else R.drawable.ic_play
    )
}

@BindingAdapter("android:onLongClick")
fun setOnLongClick(cellView: CellView, onLongClickListener: View.OnLongClickListener) {
    cellView.setOnLongClickListener(onLongClickListener)
}

@BindingAdapter("android:translationListener")
fun bindOnTranslationListener(
    constraintLayout: ConstraintLayout,
    translationListener: TranslationListener
) {
    constraintLayout.setOnDragListener(translationListener)
}