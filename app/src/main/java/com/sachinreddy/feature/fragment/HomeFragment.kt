package com.sachinreddy.feature.fragment

import android.content.Context
import android.media.*
import android.os.Bundle
import android.os.Process
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

    private var recorder: AudioRecord? = null
    private var track: AudioTrack? = null

    private val SAMPLERATE = 8000
    private val RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO
    private val TRACK_CHANNELS = AudioFormat.CHANNEL_OUT_MONO
    private val AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT

    private var minBufferSizeRec = 0
    lateinit var bufferRec: ShortArray

    private var thread: Thread? = null
    private var isRunning = false

    private var manager: AudioManager? = null

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

        minBufferSizeRec = AudioRecord.getMinBufferSize(SAMPLERATE, RECORDER_CHANNELS, AUDIO_ENCODING)
        bufferRec = ShortArray(minBufferSizeRec / 2)

        manager = activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        manager?.mode = AudioManager.MODE_NORMAL
        activity?.volumeControlStream = AudioManager.STREAM_VOICE_CALL

        recordBtn.setRecordListener(object : OnRecordListener {
            override fun onRecord() {
                (adapter.selectedCell as Cell).apply {
                    hasData = true
                    if (!isRunning) {
                        isRunning = true
                        runThread(isRunning)
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onRecordCancel() {
                if (isRunning) {
                    isRunning = false
                    runThread(isRunning)
                }
            }

            override fun onRecordFinish() {
                isRunning = false
                runThread(isRunning)
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun runThread(flag: Boolean) {
        thread = Thread(Runnable { runRunnable(flag) })
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
        thread?.start()
    }

    fun getAudioTrack() {
        val myBufferSize = AudioTrack.getMinBufferSize(SAMPLERATE, TRACK_CHANNELS, AUDIO_ENCODING)
        if (myBufferSize != AudioTrack.ERROR_BAD_VALUE) {
            track = AudioTrack(
                AudioManager.STREAM_MUSIC,
                SAMPLERATE,
                TRACK_CHANNELS,
                AUDIO_ENCODING,
                myBufferSize,
                AudioTrack.MODE_STREAM
            )
            track?.playbackRate = SAMPLERATE
            if (track?.state == AudioTrack.STATE_UNINITIALIZED) {
                println("AudioTrack Uninitialized")
                return
            }
        }
    }

    fun runRunnable(isRunning: Boolean) {
        if (!isRunning) {
            if (AudioRecord.STATE_INITIALIZED == recorder?.state) {
                recorder?.stop()
                recorder?.release()
            }
            if (track != null && AudioTrack.STATE_INITIALIZED == track?.state) {
                if (track?.playState != AudioTrack.PLAYSTATE_STOPPED) {
                    try {
                        track?.stop()
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                }
                track?.release()
                manager!!.mode = AudioManager.MODE_NORMAL
            }
            return
        } else if (isRunning) {
            recorder = findAudioRecord()
            if (recorder == null) {
                println("findAudioRecord error")
                return
            }
            getAudioTrack()
            if (track == null) {
                println( "findAudioTrack error")
                return
            }
            track?.playbackRate = SAMPLERATE
            if (AudioRecord.STATE_INITIALIZED == recorder?.state && AudioTrack.STATE_INITIALIZED == track?.state) {
                var data = ShortArray(minBufferSizeRec / 2)
                recorder?.startRecording()
                track?.play()
                while (isRunning) {
                    recorder?.read(bufferRec, 0, minBufferSizeRec / 2)
                    for (i in data.indices) {
                        data[i] = bufferRec[i]
                    }
                    track?.write(data, 0, data.size)
                    bufferRec = ShortArray(minBufferSizeRec / 2)
                    data = ShortArray(minBufferSizeRec / 2)
                }
            } else {
                println("Init for Recorder and Track failed")
                return
            }
            return
        }
        thread!!.interrupt()
    }

    private val mSampleRates = intArrayOf(8000, 11025, 22050, 44100)
    fun findAudioRecord(): AudioRecord? {
        for (rate in mSampleRates) {
            for (audioFormat in shortArrayOf(
                AudioFormat.ENCODING_PCM_8BIT.toShort(),
                AudioFormat.ENCODING_PCM_16BIT.toShort()
            )) {
                for (channelConfig in shortArrayOf(
                    AudioFormat.CHANNEL_IN_MONO.toShort(),
                    AudioFormat.CHANNEL_IN_STEREO.toShort()
                )) {
                    try {
                        val bufferSize = AudioRecord.getMinBufferSize(
                            rate,
                            channelConfig.toInt(),
                            audioFormat.toInt()
                        )
                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            val recorder = AudioRecord(
                                MediaRecorder.AudioSource.MIC,
                                rate,
                                channelConfig.toInt(),
                                audioFormat.toInt(),
                                bufferSize
                            )
                            if (recorder.state == AudioRecord.STATE_INITIALIZED) return recorder
                        }
                    } catch (e: Exception) {
                        println(rate.toString() + "Exception, keep trying.")
                    }
                }
            }
        }
        return null
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
