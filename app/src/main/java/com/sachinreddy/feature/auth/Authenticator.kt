package com.sachinreddy.feature.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sachinreddy.feature.data.Artist

object Authenticator {
    var currentUser: Artist? = null
    var currentFriends: MutableList<Artist> = mutableListOf()
    val totalFriends: MutableList<Artist>
        get() {
            var artists: MutableList<Artist> = mutableListOf()
            mDatabaseReference
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (i in snapshot.children) {
                            i.getValue(Artist::class.java)?.let {
                                if (it.artistId != currentUser?.artistId)
                                    artists.add(it)
                            }
                        }
                    }
                })
            return artists
        }

    var mAuth: FirebaseAuth = Firebase.auth
    var mDatabase: FirebaseDatabase = Firebase.database
    var mDatabaseReference: DatabaseReference = mDatabase.getReference("artists")
    var mStorage: FirebaseStorage = Firebase.storage
    var mStorageReference: StorageReference = mStorage.reference.child("artists")

    fun registerArtist(
        artistName: String,
        username: String,
        email: String,
        profilePicture: String?,
        textureBackground: String?
    ) {
        val id = mAuth.uid!!
        currentUser =
            Artist(
                id!!,
                artistName,
                username,
                email,
                "0",
                profilePicture,
                textureBackground,
                mutableListOf(),
                mutableListOf()
            )
        mDatabaseReference.child(id).setValue(currentUser)
    }

    fun logout() {
        mAuth.signOut()
        currentUser = null
    }
}