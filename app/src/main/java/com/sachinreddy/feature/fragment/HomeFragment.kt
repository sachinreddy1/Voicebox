package com.sachinreddy.feature.fragment

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.emrekose.recordbutton.OnRecordListener
import com.emrekose.recordbutton.RecordButton
import com.evrencoskun.tableview.TableView
import com.sachinreddy.feature.R
import com.sachinreddy.feature.adapter.EditCellAdapter
import com.sachinreddy.feature.adapter.EditCellListener
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.injection.appComponent
import com.sachinreddy.feature.viewModel.AppViewModel
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.table_view_cell_layout.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.util.*
import java.util.jar.Manifest
import javax.inject.Inject


const val REQUEST_PERMISSION_CODE = 1000
val PERMISSIONS = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO)

class HomeFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val appViewModel by activityViewModels<AppViewModel> { viewModelFactory }

    lateinit var mediaRecorder: MediaRecorder
    lateinit var mediaPlayer: MediaPlayer

    private var isRecording: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appComponent!!.inject(this)
        setupActionBar()

        // Setting up tableView and adapter
        val tableView = TableView(requireContext())
        val adapter = EditCellAdapter(requireContext(), appViewModel, appViewModel.mTrackList)
        tableView.adapter = adapter
        adapter.setTracks(appViewModel.mTrackList)
        content_container.adapter = adapter
        content_container.tableViewListener = EditCellListener(requireContext())

        mediaRecorder = MediaRecorder()
        ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, REQUEST_PERMISSION_CODE)
        setupMediaRecorder()

        recordBtn.setRecordListener(object : OnRecordListener {
            override fun onRecord() {
                val cell = adapter.selectedCell as Cell
                cell.hasData = true
                adapter.notifyDataSetChanged()
                startRecording()
            }

            override fun onRecordCancel() {
                stopRecording()
            }

            override fun onRecordFinish() {
                // TODO
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                validNavController?.navigate(R.id.action_HomeFragment_to_ProfileFragment)
        }
        return true
    }

    private fun setupActionBar() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(app_action_bar)
            supportActionBar?.apply {
                title = getString(R.string.app_name)
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_account_circle_dark)
                setHomeActionContentDescription(getString(R.string.open_profile_card))
            }
        }
    }

    private fun setupMediaRecorder() {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        checkPermissionFromDevice()
    }

    private fun startRecording() {
        if (isRecording) return

        try {
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(path, "/test.3gp")
            mediaRecorder.setOutputFile(file)

            mediaRecorder.prepare()
            mediaRecorder.start()

            isRecording = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        if (!isRecording) return

        mediaRecorder.stop()
        mediaRecorder.release()
        isRecording = false
    }

    private fun checkPermissionFromDevice(): Boolean {
        val write_external_storage_result = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val record_audio_format = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.RECORD_AUDIO)
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED && record_audio_format == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(requireContext(), "Permission granted.", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(requireContext(), "Permission denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val validNavController get() = findNavController().takeIf { it.valid }
    private val NavController.valid get() = currentDestination?.id == R.id.HomeFragment
}
