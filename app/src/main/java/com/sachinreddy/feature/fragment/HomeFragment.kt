package com.sachinreddy.feature.fragment

import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.emrekose.recordbutton.OnRecordListener
import com.evrencoskun.tableview.TableView
import com.sachinreddy.feature.R
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.injection.appComponent
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.table.listener.EditCellListener
import com.sachinreddy.feature.viewModel.AppViewModel
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


const val REQUEST_PERMISSION_CODE = 200
val PERMISSIONS = arrayOf(
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    android.Manifest.permission.RECORD_AUDIO
)

class HomeFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val appViewModel by activityViewModels<AppViewModel> { viewModelFactory }
    lateinit var adapter: EditCellAdapter
    private lateinit var tableView: TableView

    private val recorderThread = object : Thread() {
        override fun run() {
            recordThread()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appComponent!!.inject(this)
        setupActionBar()

        // Setting up tableView and adapter
        tableView = TableView(requireContext())
        adapter = EditCellAdapter(
            requireContext(),
            appViewModel
        )
        tableView.adapter = adapter
        adapter.setTracks(appViewModel.mTrackList)
        content_container.adapter = adapter
        content_container.tableViewListener =
            EditCellListener(
                requireContext()
            )

        ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, REQUEST_PERMISSION_CODE)

        initRecorder()
        appViewModel.audioManager =
            activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        recorderThread.start()

        recordBtn.setRecordListener(object : OnRecordListener {
            override fun onRecord() {
                (appViewModel.startingCell as Cell).apply {
                    if (!appViewModel.isRecording) {
                        startRecording(this)
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onRecordCancel() {
                if (appViewModel.isRecording) {
                    stopRecording()
                }
            }

            override fun onRecordFinish() {
                if (appViewModel.isRecording) {
                    stopRecording()
                }
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initRecorder() {
        val min = AudioRecord.getMinBufferSize(
            8000,
            AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        appViewModel.recorder = AudioRecord(
            MediaRecorder.AudioSource.VOICE_COMMUNICATION,
            8000,
            AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT,
            min
        )
        if (AcousticEchoCanceler.isAvailable()) {
            val echoCanceler = AcousticEchoCanceler.create(appViewModel.recorder!!.audioSessionId)
            echoCanceler.enabled = true
        }
    }

    private fun recordThread() {
        appViewModel.audioManager?.mode = AudioManager.MODE_IN_COMMUNICATION
        while (true) {
            appViewModel.apply {
                if (isRecording) {
                    startingCell?.let {
                        val data = ShortArray(1024)
                        recorder?.read(data, 0, 1024)
                        it.data.add(data)
                    }
                }
            }
        }
    }

    private fun startRecording(cell: Cell) {
        cell.stopTrack()
        cell.data.clear()
        appViewModel.apply {
            recorder?.startRecording()
            isRecording = true
        }
    }

    private fun stopRecording() {
        appViewModel.apply {
            recorder?.stop()
            isRecording = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_editor, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                validNavController?.navigate(R.id.action_HomeFragment_to_ProfileFragment)
            R.id.editor_actions -> {
                if (appViewModel.isSelecting) {
                    item.setIcon(R.drawable.ic_selection)
                    content_container.isFrozen = false
                    appViewModel.isSelecting = false
                } else {
                    item.setIcon(R.drawable.ic_translation)
                    content_container.isFrozen = true
                    appViewModel.isSelecting = true
                }
                adapter.notifyDataSetChanged()
            }
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

    private val validNavController get() = findNavController().takeIf { it.valid }
    private val NavController.valid get() = currentDestination?.id == R.id.HomeFragment
}
