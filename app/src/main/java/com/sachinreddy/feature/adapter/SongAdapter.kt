package com.sachinreddy.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mikhaellopez.circularimageview.CircularImageView
import com.sachinreddy.feature.R
import com.sachinreddy.feature.auth.Authenticator
import com.sachinreddy.feature.data.Artist
import com.sachinreddy.feature.data.Song

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
        private val profilePicture: CircularImageView = songView.findViewById(R.id.profilePicture)
        private val textureBackground: ImageView = songView.findViewById(R.id.textureBackground)

        var fromArtist: Artist? = null
        private val mValueEventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                fromArtist = snapshot.getValue(Artist::class.java)
                artists_.text =
                    "${fromArtist?.artistName} & ${Authenticator.currentUser?.artistName}"
                Glide
                    .with(context)
                    .load(fromArtist?.profilePicture)
                    .placeholder(R.drawable.ic_account_circle_light)
                    .into(profilePicture)

                Glide
                    .with(context)
                    .load(fromArtist?.textureBackground)
                    .placeholder(R.drawable.ic_pattern_background)
                    .into(textureBackground)
            }
        }

        fun setSongDetails(song: Song) {
            Authenticator.mDatabaseReference.child(song.from)
                .addValueEventListener(mValueEventListener)

            songTitle.text = song.songName

            favoriteButton.setOnClickListener {
                favoriteButton.setImageResource(R.drawable.ic_star_filled)
            }
        }
    }
}