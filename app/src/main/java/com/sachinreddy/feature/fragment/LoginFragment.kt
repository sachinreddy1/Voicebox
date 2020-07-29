package com.sachinreddy.feature.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.sachinreddy.feature.R
import com.sachinreddy.feature.activity.AppActivity
import com.sachinreddy.feature.auth.Authenticator
import com.sachinreddy.feature.data.Artist
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    private val mValueEventListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            val artistId = snapshot.child("artistId").value.toString()
            val artistName = snapshot.child("artistName").value.toString()
            val username = snapshot.child("username").value.toString()
            val email = snapshot.child("email").value.toString()
            val score = snapshot.child("score").value.toString()
            val profilePicture: String? = snapshot.child("profilePicture").value.toString()
            val textureBackground: String? = snapshot.child("textureBackground").value.toString()

            Authenticator.currentUser =
                Artist(
                    artistId,
                    artistName,
                    username,
                    email,
                    score,
                    profilePicture,
                    textureBackground
                )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = inputUsername.text.toString()
        val password = inputPassword.text.toString()
        buttonLogin.setOnClickListener {
            login(username, password)
        }

        buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun login(email: String, password: String) {
        // Check if that fields are not empty
        if (email.isEmpty() || password.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(
                email
            ).matches()
        ) {
            Snackbar.make(view!!, "Sign in problem.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        } else {
            // Turn on progress bar
            loginProgressBar.visibility = View.VISIBLE

            Authenticator.apply {
                // Login
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        // Get artist data
                        currentUser?.let {
                            mDatabaseReference.child(it.artistId)
                                .addValueEventListener(mValueEventListener)
                        }
                        // Turn off progress bar
                        loginProgressBar.visibility = View.GONE
                        // Open the app
                        val intent = Intent(context, AppActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        // Turn off progress bar
                        loginProgressBar.visibility = View.GONE

                        Snackbar.make(view!!, "Sign in problem.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
            }
        }
    }
}