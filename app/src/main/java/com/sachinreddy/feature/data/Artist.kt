package com.sachinreddy.feature.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Artist {
    var artistId: String? = null
    var artistName: String? = null
    var username: String? = null
    var email: String? = null
    var score: String? = null
    var profilePicture: String? = null
    var textureBackground: String? = null
    var friends: MutableList<String> = mutableListOf()
    var songs: MutableList<Song> = mutableListOf()

    constructor() {
        //this constructor is required
    }

    constructor(
        artistId: String?,
        artistName: String?,
        username: String?,
        email: String?,
        score: String?,
        profilePicture: String?,
        textureBackground: String?,
        friends: MutableList<String>,
        songs: MutableList<Song>
    ) {
        this.artistId = artistId
        this.artistName = artistName
        this.username = username
        this.email = email
        this.score = score
        this.profilePicture = profilePicture
        this.textureBackground = textureBackground
        this.friends = friends
        this.songs = songs
    }
}