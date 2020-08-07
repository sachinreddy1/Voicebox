package com.sachinreddy.feature.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Song {
    var from: String? = null
    var songName: String = "Untitled"
    var favorite: Boolean = false

    constructor() {
        //this constructor is required
    }

    constructor(
        from: String?,
        songName: String,
        favorite: Boolean
    ) {
        this.from = from
        this.songName = songName
        this.favorite = favorite
    }
}
