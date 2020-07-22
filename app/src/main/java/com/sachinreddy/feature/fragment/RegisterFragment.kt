package com.sachinreddy.feature.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sachinreddy.feature.R
import com.sachinreddy.feature.activity.AppActivity
import com.sachinreddy.feature.auth.Authenticator
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.buttonRegister
import kotlinx.android.synthetic.main.fragment_register.haveAccount
import kotlinx.android.synthetic.main.fragment_register.inputEmail
import kotlinx.android.synthetic.main.fragment_register.inputPassword

class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonRegister.setOnClickListener {
            registerAccount()
        }

        haveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
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

        login(artistName_, email_, phoneNumber_, password_)
    }

    private fun login(artistName: String, email: String, phoneNumber: String, password: String) {
        registerProgressBar.visibility = View.VISIBLE
        Authenticator.mAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Authenticator.registerArtist(artistName, email, phoneNumber, null)
                loginProgressBar.visibility = View.GONE
                val intent = Intent(context, AppActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                loginProgressBar.visibility = View.GONE
                Snackbar.make(view!!, "Failed to register user.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
    }
}