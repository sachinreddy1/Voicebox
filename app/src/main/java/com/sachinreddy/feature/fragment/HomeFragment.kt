package com.sachinreddy.feature.fragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sachinreddy.feature.R
import kotlinx.android.synthetic.main.activity_app.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(app_action_bar)
            supportActionBar?.apply {
                title = getString(R.string.app_name)
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_account_circle)
                setHomeActionContentDescription(getString(R.string.open_profile_card))
            }
        }

        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            R.id.action_start_collab ->
                Snackbar.make(view!!, "Starting a collab...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            R.id.action_add_artist ->
                Snackbar.make(view!!, "Adding a friend...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        return true
    }

    private fun resizeDrawable(id: Int, width: Int, height: Int) = BitmapDrawable(
        resources,
        Bitmap.createScaledBitmap(
            (resources.getDrawable(id) as BitmapDrawable).bitmap,
            width,
            height,
            false
        )
    )
}
