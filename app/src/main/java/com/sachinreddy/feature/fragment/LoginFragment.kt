package com.sachinreddy.feature.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
            val email = snapshot.child("email").value.toString()
            val phoneNumber = snapshot.child("phoneNumber").value.toString()
            val profilePicture: String? = snapshot.child("profilePicture").value.toString()

            Authenticator.currentUser =
                Artist(artistId, artistName, email, phoneNumber, profilePicture)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonLogin.setOnClickListener {
            authenticate(inputEmail.text.toString(), inputPassword.text.toString())
        }

        buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun authenticate(email: String, password: String) {
        loginProgressBar.visibility = View.VISIBLE
        if (email.isEmpty() || password.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(
                email
            ).matches()
        ) {
            Toast.makeText(context, "Sign in problem", Toast.LENGTH_LONG).show()
        } else {
            Authenticator.mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Authenticator.mDatabaseReference.child(Authenticator.mAuth.currentUser?.uid!!)
                        .addValueEventListener(mValueEventListener)
                    val intent = Intent(context, AppActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Sign in problem", Toast.LENGTH_LONG).show()
                }
            loginProgressBar.visibility = View.GONE
        }
    }
}