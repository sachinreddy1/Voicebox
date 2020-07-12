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
import com.google.firebase.ktx.Firebase
import com.sachinreddy.feature.R
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = Firebase.auth

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
        }

        mAuth.createUserWithEmailAndPassword(email_, password_).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "User Registered is Successful", Toast.LENGTH_LONG).show()
            }
        }
    }
}