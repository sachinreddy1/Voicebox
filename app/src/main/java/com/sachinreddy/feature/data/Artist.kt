package com.sachinreddy.feature.data

data class Artist(
    val artistId: String,
    var artistName: String,
    val username: String,
    val email: String,
    var score: String,
    var profilePicture: String?,
    var textureBackground: String?
)