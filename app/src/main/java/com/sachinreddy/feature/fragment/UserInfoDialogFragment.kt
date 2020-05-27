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

        // TODO("Is there a better way to do this?")
//        database.getItems().observe(viewLifecycleOwner, Observer {
//            val sections = it.map { it.section }.distinct()
//            val autoAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, sections)
//            artistName_textEdit.setAdapter(autoAdapter)
//            artistName_textEdit.threshold = 1
//        })

        println("View created.")

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