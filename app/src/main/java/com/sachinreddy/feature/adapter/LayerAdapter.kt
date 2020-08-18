package com.sachinreddy.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachinreddy.feature.R
import kotlinx.android.synthetic.main.item_layer.view.*

class LayerAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layer, parent, false)
        return LayerViewHolder(view)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.editCells.adapter = CellAdapter(context)
    }

    internal inner class LayerViewHolder(timeView: View) : RecyclerView.ViewHolder(timeView) {

    }
}