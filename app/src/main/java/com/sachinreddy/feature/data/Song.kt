package com.sachinreddy.feature.data

data class Song(
    val artists: List<Artist>,
    val from: Artist,
    var songName: String,
    val favorite: Boolean
)