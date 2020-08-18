package com.sachinreddy.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachinreddy.feature.R

class CellAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cell, parent, false)
        return EditCellViewHolder(view)
    }

    override fun getItemCount(): Int = Integer.MAX_VALUE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as CellAdapter.EditCellViewHolder).setBarNumber(position)

    internal inner class EditCellViewHolder(timeView: View) : RecyclerView.ViewHolder(timeView) {
        fun setBarNumber(position: Int) {

        }
    }
}