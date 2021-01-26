package com.sachinreddy.feature.fragment

import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.emrekose.recordbutton.OnRecordListener
import com.sachinreddy.feature.R
import com.sachinreddy.feature.databinding.FragmentHomeBinding
import com.sachinreddy.feature.injection.appComponent
import com.sachinreddy.feature.table.adapter.EditCellAdapter
import com.sachinreddy.feature.table.listener.EditCellListener
import com.sachinreddy.feature.viewModel.AppViewModel
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
    private lateinit var appViewModel: AppViewModel
    private lateinit var binding: FragmentHomeBinding

    private val recorderThread = object : Thread() {
        override fun run() {
            recordThread()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponent!!.inject(this)

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        ViewModelProvider(this@HomeFragment, viewModelFactory).get(AppViewModel::class.java).let {
            binding.vm = it
            appViewModel = it
        }

        binding.contentContainer.apply {
            this.adapter = EditCellAdapter(
                requireContext(),
                appViewModel
            )
            this.tableViewListener = EditCellListener(requireContext())
            appViewModel.tableView = this
        }

        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupActionBar()

        ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, REQUEST_PERMISSION_CODE)

        initRecorder()
        appViewModel.audioManager =
            activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        recorderThread.start()

        recordBtn.setRecordListener(object : OnRecordListener {
            override fun onRecord() {
                if (!appViewModel.isRecording) {
                    startRecording()
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
                    val newCells = cells.value?.map { track ->
                        track.map { cell ->
                            if (cell.isSelected) {
                                val data = ShortArray(1024)
                                recorder?.read(data, 0, 1024)
                                cell.data.add(data)
                            }
                            cell
                        }
                        track
                    }

                    cells.postValue(newCells)
                }
            }
        }
    }

    private fun startRecording() {
        appViewModel.apply {
            for (cell in selectedCells) {
                stopTrack(cell)
                cell.data.clear()
            }

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
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_reset ->
                Toast.makeText(requireContext(), "RESETTING", Toast.LENGTH_SHORT).show()
            R.id.action_download ->
                Toast.makeText(requireContext(), "DOWNLOADING", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private fun setupActionBar() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(app_action_bar)
            supportActionBar?.apply {
                title = getString(R.string.app_name)
            }
        }
    }

    private val validNavController get() = findNavController().takeIf { it.valid }
    private val NavController.valid get() = currentDestination?.id == R.id.HomeFragment
}
