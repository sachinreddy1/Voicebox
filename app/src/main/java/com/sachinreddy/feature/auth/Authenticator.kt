package com.sachinreddy.feature.auth

import android.net.Uri
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
    var mStorageReference: StorageReference = mStorage.reference

    fun registerArtist(
        artistName: String,
        email: String,
        phoneNumber: String,
        profilePicture: StorageReference?
    ) {
        val id = mAuth.uid!!
        currentUser = Artist(id!!, artistName, email, phoneNumber, profilePicture)
        mDatabaseReference.child(id).setValue(currentUser)
    }

    fun uploadProfilePicture(filePath: Uri): Boolean {
        var ret = false
        val id = mAuth.uid!!

        if (filePath != null) {
            val ref = mStorageReference.child("artists/$id")
            ref.putFile(filePath)
                .addOnSuccessListener {
                    currentUser?.profilePicture = mStorageReference.child(id)
                    mDatabaseReference.child(id).setValue(currentUser)
                    ret = true
                }

                .addOnFailureListener {
                    ret = false
                }
        }

        return ret
    }

    fun logout() {
        mAuth.signOut()
        currentUser = null
    }
}