package com.sachinreddy.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sachinreddy.feature.R

class TimelineAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_timeline, parent, false)
        return TimelineViewHolder(view)
    }

    override fun getItemCount(): Int = Integer.MAX_VALUE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as TimelineAdapter.TimelineViewHolder).setBarNumber(position)

    internal inner class TimelineViewHolder(timeView: View) : RecyclerView.ViewHolder(timeView) {
        private val barNumber: TextView = timeView.findViewById(R.id.barNumber)

        fun setBarNumber(position: Int) {
            barNumber.text = (position + 1).toString()
        }
    }
}