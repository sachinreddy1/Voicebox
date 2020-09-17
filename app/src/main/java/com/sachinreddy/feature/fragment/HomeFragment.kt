package com.sachinreddy.feature.fragment

import android.content.Context
import android.media.*
import android.media.audiofx.AcousticEchoCanceler
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import com.sachinreddy.feature.adapter.EditCellAdapter
import com.sachinreddy.feature.adapter.EditCellListener
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.injection.appComponent
import com.sachinreddy.feature.viewModel.AppViewModel
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


const val REQUEST_PERMISSION_CODE = 200
val PERMISSIONS = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO)

class HomeFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val appViewModel by activityViewModels<AppViewModel> { viewModelFactory }

    var isRecording = false
    var audioManager: AudioManager? = null
    var record: AudioRecord? = null
    var track: AudioTrack? = null

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

        ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, REQUEST_PERMISSION_CODE)

        initRecordAndTrack()
        audioManager = activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        object : Thread() {
            override fun run() {
                recordAndPlay()
            }
        }.start()

        recordBtn.setRecordListener(object : OnRecordListener {
            override fun onRecord() {
                (adapter.selectedCell as Cell).apply {
                    hasData = true
                    if (!isRecording) {
                        startRecordAndPlay()
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onRecordCancel() {
                if (isRecording) {
                    stopRecordAndPlay();
                }
            }

            override fun onRecordFinish() {
                if (isRecording) {
                    stopRecordAndPlay();
                }
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initRecordAndTrack() {
        val min = AudioRecord.getMinBufferSize(
            8000,
            AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        record = AudioRecord(
            MediaRecorder.AudioSource.VOICE_COMMUNICATION,
            8000,
            AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT,
            min
        )
        if (AcousticEchoCanceler.isAvailable()) {
            val echoCanceler = AcousticEchoCanceler.create(record!!.audioSessionId)
            echoCanceler.enabled = true
        }
        val maxJitter = AudioTrack.getMinBufferSize(
            8000,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        track = AudioTrack(
            AudioManager.MODE_IN_COMMUNICATION,
            8000,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT,
            maxJitter,
            AudioTrack.MODE_STREAM
        )
    }

    private fun recordAndPlay() {
        val lin = ShortArray(1024)
        var num = 0

        audioManager?.mode = AudioManager.MODE_IN_COMMUNICATION
        while (true) {
            if (isRecording) {
                num = record!!.read(lin, 0, 1024)
                track!!.write(lin, 0, num)
            }
        }
    }

    private fun startRecordAndPlay() {
        record?.startRecording()
        track?.play()
        isRecording = true
    }

    private fun stopRecordAndPlay() {
        record?.stop()
        track?.pause()
        isRecording = false
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

    private val validNavController get() = findNavController().takeIf { it.valid }
    private val NavController.valid get() = currentDestination?.id == R.id.HomeFragment
}
