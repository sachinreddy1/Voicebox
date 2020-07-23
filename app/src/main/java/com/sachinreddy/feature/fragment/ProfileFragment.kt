package com.sachinreddy.feature.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sachinreddy.feature.R
import com.sachinreddy.feature.activity.AuthActivity
import com.sachinreddy.feature.auth.Authenticator
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.IOException


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ProfileFragment : Fragment() {
    private lateinit var filePath: Uri
    private val PICK_IMAGE_REQUEST = 71
    private var uploadImage = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Authenticator.currentUser?.profilePicture?.let {
            Glide
                .with(this)
                .load(it)
                .placeholder(R.drawable.ic_account_circle_light)
                .dontAnimate()
                .preload()
        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupActionBar()

        artistName.text = Authenticator.currentUser?.artistName
        contactInfo.text = Authenticator.currentUser?.email
        Authenticator.currentUser?.profilePicture?.let {
            Glide
                .with(this)
                .load(it)
                .placeholder(R.drawable.ic_account_circle_light)
                .dontAnimate()
                .into(profilePicture)
        }

        editButton.setOnClickListener {
            if (!uploadImage) {
                chooseImage()
            } else {
                uploadProfilePicture(filePath)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                findNavController().navigate(R.id.action_ProfileFragment_to_HomeFragment)
            R.id.action_about ->
                Snackbar.make(view!!, "Opening settings...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            R.id.action_logout -> {
                Authenticator.logout()
                val intent = Intent(context, AuthActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null && data.data != null) {
            filePath = data.data!!
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(activity!!.contentResolver, filePath)
                profilePicture.setImageBitmap(bitmap)
                editButton.setImageResource(R.drawable.ic_send)
                uploadImage = true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadProfilePicture(filePath: Uri) {
        Authenticator.currentUser?.artistId?.let { id ->
            if (filePath != null) {
                Authenticator.mStorageReference.child(id).putFile(filePath)
                    .addOnSuccessListener {
                        Authenticator.mStorageReference.child(id).downloadUrl
                            .addOnSuccessListener {
                                Authenticator.currentUser?.profilePicture = it.toString()
                                Authenticator.mDatabaseReference.child(id)
                                    .setValue(Authenticator.currentUser)
                            }
                            .addOnFailureListener {
                                println(it)
                            }

                        editButton.setImageResource(R.drawable.ic_edit)
                        profileProgressBar.visibility = View.GONE
                        uploadImage = false
                        Snackbar.make(view!!, "File upload successful!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                    .addOnFailureListener {
                        editButton.setImageResource(R.drawable.ic_edit)
                        profileProgressBar.visibility = View.GONE
                        uploadImage = false

                        Snackbar.make(view!!, "File upload failed.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                    .addOnProgressListener {
                        profileProgressBar.visibility = View.VISIBLE
                    }
            }
        }
    }

    private fun setupActionBar() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(app_action_bar)
            supportActionBar?.apply {
                title = getString(R.string.profile)
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_arrow_back)
                setHomeActionContentDescription(getString(R.string.back_to_home))
            }
        }
    }
}
