package com.sachinreddy.feature.data

import android.net.Uri

data class Artist(
    val artistId: String,
    var artistName: String,
    val email: String,
    val phoneNumber: String,
    var profilePicture: Uri?
)