package com.sachinreddy.feature.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sachinreddy.feature.R
import com.sachinreddy.feature.activity.AppActivity
import com.sachinreddy.feature.auth.Authenticator
import com.sachinreddy.feature.injection.ApplicationComponent
import com.sachinreddy.feature.injection.DaggerApplicationComponent
import com.sachinreddy.feature.modules.ApplicationModule
import com.sachinreddy.feature.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject


class LoginFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val authViewModel by activityViewModels<AuthViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val component: ApplicationComponent by lazy {
            DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(activity?.application!!))
                .build()
        }
        component.inject(this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonLogin.setOnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            login(email, password)
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
            Snackbar.make(requireView(), "Sign in problem.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        } else {
            // Turn on progress bar
            loginProgressBar.visibility = View.VISIBLE

            Authenticator.apply {
                // Login
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        // Get artist data
                        mDatabaseReference.child(mAuth.uid!!)
                            .addListenerForSingleValueEvent(authViewModel.mValueEventListener)
                            .also {
                                // Turn off progress bar
                                loginProgressBar.visibility = View.GONE
                                // Open the app
                                val intent = Intent(context, AppActivity::class.java)
                                startActivity(intent)
                            }
                        mDatabaseReference
                            .addValueEventListener(authViewModel.mFriendsValueEventListener)
                    }
                    .addOnFailureListener {
                        // Turn off progress bar
                        loginProgressBar.visibility = View.GONE

                        Snackbar.make(requireView(), "Sign in problem.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
            }
        }
    }
}