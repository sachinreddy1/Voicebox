package com.sachinreddy.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.Song
import de.hdodenhof.circleimageview.CircleImageView


class SongAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var songs: List<Song> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int = songs.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as SongViewHolder).setSongDetails(songs[position])

    internal inner class SongViewHolder(songView: View) : RecyclerView.ViewHolder(songView) {
        private val songTitle: TextView = songView.findViewById(R.id.songTitle)
        private val artists_: TextView = songView.findViewById(R.id.artists)
        private val favoriteButton: ImageView = songView.findViewById(R.id.favoriteButton)
        private val playButton: ImageView = songView.findViewById(R.id.playButton)
        private val circleImageView: CircleImageView = songView.findViewById(R.id.circleImageView)

        fun setSongDetails(song: Song) {
            song.apply {
                songTitle.text = songName
                artists_.text = from.artistName
                Glide
                    .with(context)
                    .load(from.profilePicture)
                    .placeholder(R.drawable.ic_account_circle_light)
                    .dontAnimate()
                    .into(circleImageView)
            }

            favoriteButton.setOnClickListener {
                favoriteButton.setImageResource(R.drawable.ic_star_filled)
            }
        }
    }
}