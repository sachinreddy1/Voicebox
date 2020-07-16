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
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onStart() {
//        setHasOptionsMenu(false)
//        (requireActivity() as AppCompatActivity).apply {
//            supportActionBar?.apply {
//                title = getString(R.string.app_name)
//                setDisplayHomeAsUpEnabled(false)
//            }
//        }
        mAuth.addAuthStateListener(mAuthListener)
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = Firebase.auth
        mAuthListener = FirebaseAuth.AuthStateListener {
//            if (it.currentUser != null) {
//                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
//            }
        }

        buttonLogin.setOnClickListener {
            authenticate(inputEmail.text.toString(), inputPassword.text.toString())
        }

        buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun authenticate(email: String, password: String) {
        loginProgressBar.visibility = View.VISIBLE
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Sign in problem", Toast.LENGTH_LONG).show()
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                loginProgressBar.visibility = View.GONE
                if (!it.isSuccessful) {
                    Toast.makeText(context, "Sign in problem", Toast.LENGTH_LONG).show()
                } else {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }
    }
}