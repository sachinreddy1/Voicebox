package com.sachinreddy.feature.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.Artist
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = Firebase.auth
        mDatabase = Firebase.database
        mReference = mDatabase.getReference("artists")

        buttonRegister.setOnClickListener {
            registerAccount()
        }

        haveAccount.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun registerAccount() {
        val artistName_ = artistName.text.toString().trim()
        val email_ = inputEmail.text.toString().trim()
        val phoneNumber_ = phoneNumber.text.toString().trim()
        val password_ = inputPassword.text.toString().trim()
        val confirmPassword_ = confirmPassword.text.toString().trim()

        if (artistName_.isEmpty()) {
            artistName.error = "Artist name is required."
            artistName.requestFocus()
            return
        }

        if (email_.isEmpty()) {
            inputEmail.error = "Email is required."
            inputEmail.requestFocus()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_).matches()) {
            inputEmail.error = "Please enter a valid email address.."
            inputEmail.requestFocus()
            return
        }

        if (phoneNumber_.isEmpty()) {
            phoneNumber.error = "Phone number is required."
            phoneNumber.requestFocus()
            return
        }

        if (password_.isEmpty()) {
            inputPassword.error = "Password is required."
            inputPassword.requestFocus()
            return
        }

        if (confirmPassword_.isEmpty()) {
            confirmPassword.error = "Please confirm your password."
            confirmPassword.requestFocus()
            return
        }

        if (password_ != confirmPassword_) {
            inputPassword.error = "Passwords do not match."
            inputPassword.requestFocus()
            return
        }

        registerProgressBar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email_, password_).addOnCompleteListener {
            if (it.isSuccessful) {
                registerArtist(artistName_, email_, phoneNumber_)
                registerProgressBar.visibility = View.GONE
                Toast.makeText(context, "User Registered is Successful", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registerArtist(artistName: String, email: String, phoneNumber: String) {
        val id = mReference.push().key
        val artist = Artist(id!!, artistName, email, phoneNumber)
        mReference.child(mAuth.uid!!).setValue(artist)
    }
}