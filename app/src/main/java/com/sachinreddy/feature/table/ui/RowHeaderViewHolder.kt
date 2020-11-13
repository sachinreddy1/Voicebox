package com.sachinreddy.feature.table.ui

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.Track
import com.sachinreddy.feature.data.table.RowHeader
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.viewModel.AppViewModel

class RowHeaderViewHolder(
    layout: View,
    private val appViewModel: AppViewModel,
    private val editCellAdapter: EditCellAdapter
) : AbstractViewHolder(layout) {
    val row_header_container: ConstraintLayout = layout.findViewById(R.id.row_header_container)
    val row_header_imageView: ImageView = layout.findViewById(R.id.row_header_imageView)
    val row_header_button_container: ConstraintLayout = layout.findViewById(R.id.row_header_button_container)
    val row_header_button: ImageButton = layout.findViewById(R.id.row_header_button)

    fun bind(rowPosition: Int) {
        row_header_container.apply {
            setOnClickListener {
                if (appViewModel.isSelecting) {
                    editCellAdapter.clearSelectedCells()
                    for (i in 0..appViewModel.numberBars) {
                        val cell = editCellAdapter.getCellItem(i, rowPosition)
                        cell?.let {
                            it.isSelected = true
                        }
                    }
                    editCellAdapter.notifyDataSetChanged()
                }
            }
        }

        row_header_button.apply {
            setOnClickListener {
                appViewModel.mTrackList.add(
                    Track(
                        RowHeader(""),
                        appViewModel.numberBars,
                        rowPosition + 1
                    )
                )
                editCellAdapter.setTracks(appViewModel.mTrackList)
            }
        }

        // Set the add button at the bottom of the rowHeaders
        row_header_button_container.visibility = if (rowPosition == appViewModel.mTrackList.size - 1) View.VISIBLE else View.GONE
    }
}