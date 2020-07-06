package com.sachinreddy.feature.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import com.android.volley.toolbox.Volley.newRequestQueue
import com.google.android.material.snackbar.Snackbar
import com.sachinreddy.feature.FileDataPart
import com.sachinreddy.feature.R
import com.sachinreddy.feature.VolleyFileUploadRequest
import com.sachinreddy.feature.database.MyAppDatabase
import com.sachinreddy.feature.database.MyDao
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.IOException

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ProfileFragment : Fragment() {
    private lateinit var database: MyDao
    private var imageData: ByteArray? = null
    private val postURL: String = "https://ptsv2.com/t/54odo-1576291398/post"

    companion object {
        private const val IMAGE_PICK_CODE = 999
    }

    override fun onStart() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(profileToolbar)
            supportActionBar?.apply {
                title = getString(R.string.profile)
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_arrow_back_black)
                setHomeActionContentDescription(getString(R.string.back_to_home))
            }
        }
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = MyAppDatabase.getInstance(requireContext()).MyDao()

        database.getUserInfo().observe(viewLifecycleOwner, Observer {
            artistName.text = it[0].artistName
        })
        database.getUserInfo().observe(viewLifecycleOwner, Observer {
            contactInfo.text = it[0].email
        })

        uploadButton.setOnClickListener {
            launchGallery()
        }
        sendButton.setOnClickListener {
            uploadImage()
        }
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
                findNavController().popBackStack()
            R.id.button_settings ->
                Snackbar.make(view!!, "Opening settings...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        return true
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            IMAGE_PICK_CODE
        )
    }

    private fun uploadImage() {
        imageData ?: return
        val request = object : VolleyFileUploadRequest(
            Method.POST,
            postURL,
            Response.Listener {
                println("response is: ${it.data}")
            },
            Response.ErrorListener {
                println("error is: $it")
            }
        ) {
            override fun getByteData(): MutableMap<String, FileDataPart> {
                val params = HashMap<String, FileDataPart>()
                params["imageFile"] = FileDataPart(
                    "image",
                    imageData!!,
                    "jpeg"
                )
                return params
            }
        }
        newRequestQueue(requireContext()).add(request)
    }

    @Throws(IOException::class)
    private fun createImageData(uri: Uri) {
        val inputStream = activity!!.contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            imageData = it.readBytes()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val uri = data?.data
            if (uri != null) {
                profilePicture.setImageURI(uri)
                createImageData(uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
