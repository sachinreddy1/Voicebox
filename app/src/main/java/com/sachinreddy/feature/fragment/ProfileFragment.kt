package com.sachinreddy.feature.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
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
    private var uploadingTexture = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup actionBar
        setupActionBar()
        // Setup artist information
        artistName.text = Authenticator.currentUser?.artistName
        username.text = Authenticator.currentUser?.username
        artistScore.text = Authenticator.currentUser?.score.toString()
        // Setup profile picture
        Authenticator.currentUser?.profilePicture?.let {
            Glide
                .with(this)
                .load(it)
                .placeholder(R.drawable.doggi_target)
                .into(profilePicture)
        }
        // Setup texture background
        Authenticator.currentUser?.textureBackground?.let {
            Glide
                .with(this)
                .load(it)
                .placeholder(R.drawable.ic_pattern_background)
                .centerCrop()
                .into(textureBackground)
        }

        editButton.setOnClickListener {
            if (!uploadImage) {
                val popup = PopupMenu(context, editButton)
                popup.inflate(R.menu.menu_picture)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_edit_profile_picture -> {
                            uploadingTexture = false
                            chooseImage()
                            true
                        }
                        R.id.action_edit_background -> {
                            uploadingTexture = true
                            chooseImage()
                            true
                        }
                        else -> true
                    }
                }
                popup.show()
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
                validNavController?.navigate(R.id.action_ProfileFragment_to_HomeFragment)
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

    private fun uploadProfilePicture(filePath: Uri) {
        Authenticator.currentUser?.artistId?.let { id ->
            if (filePath != null) {
                // Getting the storage reference
                val currentImage = if (!uploadingTexture) "profilePicture" else "textureBackground"
                val storageReference = Authenticator.mStorageReference
                    .child(id)
                    .child(currentImage)

                // Add photo to storage
                storageReference.putFile(filePath)
                    .addOnSuccessListener {
                        // Get the download URL
                        storageReference.downloadUrl
                            .addOnSuccessListener {
                                // Add URL to current user
                                if (!uploadingTexture) {
                                    Authenticator.currentUser?.profilePicture = it.toString()
                                } else {
                                    Authenticator.currentUser?.textureBackground = it.toString()
                                }

                                // Update the database
                                Authenticator.mDatabaseReference
                                    .child(id)
                                    .setValue(Authenticator.currentUser)
                            }
                            .addOnFailureListener {
                                println(it)
                            }

                        // Set button back to edit and remove progress bar
                        editButton.setImageResource(R.drawable.ic_edit)
                        profileProgressBar.visibility = View.GONE
                        uploadImage = false

                        Snackbar.make(view!!, "File upload successful!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                    .addOnFailureListener {
                        // Set button back to edit and remove progress bar
                        editButton.setImageResource(R.drawable.ic_edit)
                        profileProgressBar.visibility = View.GONE
                        uploadImage = false

                        Snackbar.make(view!!, "File upload failed.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                    .addOnProgressListener {
                        // Progress bar for image upload
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null && data.data != null) {
            filePath = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, filePath)
                // Set the bitmap to either profile picture or the background
                if (!uploadingTexture) {
                    profilePicture.scaleType = ImageView.ScaleType.CENTER_CROP
                    profilePicture.setImageBitmap(bitmap)
                } else {
                    textureBackground.scaleType = ImageView.ScaleType.CENTER_CROP
                    textureBackground.setImageBitmap(bitmap)
                }

                // Set button to send
                editButton.setImageResource(R.drawable.ic_send)
                uploadImage = true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private val validNavController get() = findNavController().takeIf { it.valid }
    private val NavController.valid get() = currentDestination?.id == R.id.ProfileFragment
}
