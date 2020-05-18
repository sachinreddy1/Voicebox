package com.sachinreddy.feature

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ProfileFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).apply {
            supportActionBar?.apply {
                title = getString(R.string.profile)
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_arrow_back_black)
                setHomeActionContentDescription(getString(R.string.back_to_home))
            }
        }

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            R.id.button_settings ->
                Snackbar.make(view!!, "Opening settings...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        return true
    }
}
