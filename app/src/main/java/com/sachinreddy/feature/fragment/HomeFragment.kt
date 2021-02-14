package com.sachinreddy.feature.fragment

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sachinreddy.feature.R
import com.sachinreddy.feature.databinding.FragmentHomeBinding
import com.sachinreddy.feature.injection.appComponent
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
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponent!!.inject(this)

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm =
            ViewModelProvider(this@HomeFragment, viewModelFactory).get(AppViewModel::class.java)

        (binding.vm)?.tableView = binding.contentContainer

        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupActionBar()

        ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, REQUEST_PERMISSION_CODE)
        (binding.vm)?.audioManager =
            activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        super.onViewCreated(view, savedInstanceState)
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
}
