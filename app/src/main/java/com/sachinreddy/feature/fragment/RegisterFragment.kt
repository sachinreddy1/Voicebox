package com.sachinreddy.feature.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.User
import com.sachinreddy.feature.database.MyAppDatabase
import kotlinx.android.synthetic.main.fragment_login.*

class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = MyAppDatabase.getInstance(requireContext()).MyDao()

        buttonLogin.setOnClickListener {
            database.addUserInfo(
                User(
                    artistName = "${inputEmail.text}",
                    email = "${inputPassword.text}"
                )
            )
        }

        haveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}