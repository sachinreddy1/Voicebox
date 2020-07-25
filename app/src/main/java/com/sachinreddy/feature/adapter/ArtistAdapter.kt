package com.sachinreddy.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.Artist
import de.hdodenhof.circleimageview.CircleImageView

class ArtistAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var artists: List<Artist> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_artist, parent, false)
        return ArtistViewHolder(view)
    }

    override fun getItemCount(): Int = artists.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as ArtistViewHolder).setArtistDetails(artists[position])

    internal inner class ArtistViewHolder(artistView: View) : RecyclerView.ViewHolder(artistView) {
        private val artistName: TextView = artistView.findViewById(R.id.artistName)
        private val artistEmail: TextView = artistView.findViewById(R.id.artistEmail)
        private val circleImageView: CircleImageView = artistView.findViewById(R.id.circleImageView)

        fun setArtistDetails(artist: Artist) {
            artistName.text = artist.artistName
            artistEmail.text = artist.email
            Glide
                .with(context)
                .load(artist.profilePicture)
                .placeholder(R.drawable.ic_account_circle_light)
                .dontAnimate()
                .into(circleImageView)
        }
    }
}