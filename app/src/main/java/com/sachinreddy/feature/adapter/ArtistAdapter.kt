package com.sachinreddy.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.Artist

class ArtistAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var artists: List<Artist> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)
        return ArtistViewHolder(view)
    }

    override fun getItemCount(): Int = artists.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as ArtistViewHolder).setArtistDetails(artists[position])

    internal inner class ArtistViewHolder(artistView: View) : RecyclerView.ViewHolder(artistView) {
        fun setArtistDetails(artist: Artist) {
            TODO()
        }
    }
}