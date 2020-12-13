package com.sachinreddy.feature.table.ui

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.RowHeader
import com.sachinreddy.feature.databinding.TableViewRowHeaderLayoutBinding
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.viewModel.AppViewModel

class RowHeaderViewHolder(
    layout: View,
    private val appViewModel: AppViewModel,
    private val editCellAdapter: EditCellAdapter
) : AbstractViewHolder(layout) {
    val row_header_container: ConstraintLayout = layout.findViewById(R.id.row_header_container)
    val row_header_button: ImageButton = layout.findViewById(R.id.row_header_button)

    private var _rowHeader: RowHeader? = null

    var rowHeader: RowHeader?
        set(value) {
            _rowHeader = value
            binding?.rowHeader = value
            binding?.vm = appViewModel

            row_header_container.apply {
                setOnClickListener {
                    if (appViewModel.isSelecting) {
                        editCellAdapter.clearSelectedCells()
                        for (i in 0..appViewModel.numberBars) {
                            editCellAdapter.getCellItem(i, _rowHeader?.rowPosition ?: 0)?.let {
                                it.isSelected = true
                                appViewModel.selectedCells.add(it)
                            }
                        }

                        editCellAdapter.notifyDataSetChanged()
                    }
                }
            }

            row_header_button.apply {
                setOnClickListener {
//                    appViewModel.mTrackList.add(
//                        Track(
//                            RowHeader(""),
//                            appViewModel.numberBars,
//                            rowPosition + 1
//                        )
//                    )
//                    editCellAdapter.setTracks(appViewModel.mTrackList)
                }
            }

            binding?.executePendingBindings()
        }
        get() = _rowHeader

    private val binding: TableViewRowHeaderLayoutBinding? = try { DataBindingUtil.bind(itemView) } catch (t: Throwable) { null }
}