package com.sachinreddy.feature.data

import com.google.firebase.storage.StorageReference

data class Artist(
    val artistId: String,
    var artistName: String,
    val email: String,
    val phoneNumber: String,
    var profilePicture: StorageReference?
)