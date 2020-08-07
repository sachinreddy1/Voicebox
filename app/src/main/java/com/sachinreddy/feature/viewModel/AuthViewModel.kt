package com.sachinreddy.feature.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.sachinreddy.feature.auth.Authenticator
import com.sachinreddy.feature.data.Artist
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthViewModel @Inject constructor() : ViewModel() {
    val mFriendsValueEventListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            Authenticator.apply {
                currentFriends.clear()
                currentUser?.friends?.let { artists ->
                    for (i in artists) {
                        snapshot.child(i).getValue(Artist::class.java)?.let { artist ->
                            currentFriends.add(artist)
                        }
                    }
                }
                currentFriends.sortWith(compareBy<Artist> { it.artistName }.thenBy { it.username })
            }
        }
    }
}