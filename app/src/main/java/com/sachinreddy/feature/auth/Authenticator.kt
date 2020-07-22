package com.sachinreddy.feature.auth

import android.net.Uri
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sachinreddy.feature.data.Artist

object Authenticator {
    var currentUser: Artist? = null
    var mAuth: FirebaseAuth = Firebase.auth
    var mDatabase: FirebaseDatabase = Firebase.database
    var mDatabaseReference: DatabaseReference = mDatabase.getReference("artists")
    var mArtistReference = mDatabaseReference.child(mAuth.currentUser?.uid!!)
    var mStorage: FirebaseStorage = Firebase.storage
    var mStorageReference: StorageReference = mStorage.reference.child("artists")

    fun registerArtist(
        artistName: String,
        email: String,
        phoneNumber: String,
        profilePicture: String?
    ) {
        val id = mAuth.uid!!
        currentUser = Artist(id!!, artistName, email, phoneNumber, profilePicture)
        mDatabaseReference.child(id).setValue(currentUser)
    }

    fun uploadProfilePicture(filePath: Uri, view: View) {
        currentUser?.artistId?.let { id ->
            if (filePath != null) {
                mStorageReference.child(id).putFile(filePath)
                    .addOnSuccessListener {
                        mStorageReference.child(id).downloadUrl
                            .addOnSuccessListener {
                                currentUser?.profilePicture = it.toString()
                                mDatabaseReference.child(id).setValue(currentUser)
                            }
                            .addOnFailureListener {
                                println(it)
                            }
                        Snackbar.make(view, "File upload successful!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                    .addOnFailureListener {
                        Snackbar.make(view, "File upload failed.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
            }

        }

    }

    fun logout() {
        mAuth.signOut()
        currentUser = null
    }
}