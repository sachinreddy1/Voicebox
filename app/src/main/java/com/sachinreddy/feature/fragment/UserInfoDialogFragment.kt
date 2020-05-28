package com.sachinreddy.feature.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.User
import com.sachinreddy.feature.database.MyAppDatabase
import kotlinx.android.synthetic.main.dialog_user_info.*

class UserInfoDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_user_info, container, false)

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
            dismiss()
        }

        cancel_item_button.setOnClickListener {
            dismiss()
        }
    }
}