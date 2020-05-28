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

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = MyAppDatabase.getInstance(requireContext()).MyDao()

        add_item_button.setOnClickListener {
            database.addUserInfo(
                User(
                    artistName = "${artistName_textEdit.text}",
                    email = "${email_textEdit.text}"
                )
            )
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }
}