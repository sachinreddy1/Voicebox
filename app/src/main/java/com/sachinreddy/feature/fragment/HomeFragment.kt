package com.sachinreddy.feature.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sachinreddy.feature.R
import com.sachinreddy.feature.databinding.FragmentHomeBinding
import com.sachinreddy.feature.viewModel.AppViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : Fragment() {
    lateinit var appViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = this@HomeFragment

            ViewModelProvider(this@HomeFragment, viewModelFactory).get(AppViewModel::class.java)
                .apply {
                    vm = this
                    appViewModel = this
                }

            vm?.tableView = binding.contentContainer
        }

        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupActionBar()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_reset ->
                appViewModel.initEditor()
            R.id.action_info ->
                Toast.makeText(requireContext(), "Beta v0.1", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private fun setupActionBar() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(app_action_bar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setLogo(R.drawable.ic_music_note)
            }
        }
    }
}
