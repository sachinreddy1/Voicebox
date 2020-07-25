package com.sachinreddy.feature.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sachinreddy.feature.R
import com.sachinreddy.feature.adapter.SongAdapter
import com.sachinreddy.feature.data.Artist
import com.sachinreddy.feature.data.Song
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.fragment_home.*

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
        setupActionBar()
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
                findNavController().navigate(R.id.action_homeFragment_to_friendsFragment)
            R.id.action_add_artist ->
                Snackbar.make(view!!, "Adding a friend...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = SongAdapter(context!!)
        songs_recycler_view.adapter = adapter

        // Songs go here
        val artist1 = Artist(
            "",
            "Artist Name",
            "test1@test1.com",
            "",
            "https://firebasestorage.googleapis.com/v0/b/collab-c4a6e.appspot.com/o/artists%2FcQcGlyqMi5SszPN4dBKhjR3WgG63?alt=media&token=99c3c99b-7485-43df-a0ec-4974cae1a227"
        )
        val artist2 = Artist(
            "",
            "test2",
            "test2@test2.com",
            "",
            "https://i1.sndcdn.com/avatars-000701366305-hu9f0i-t500x500.jpg"
        )

        val songs_: List<Song> = listOf(
            Song(
                listOf(
                    artist1,
                    artist2
                ),
                artist2,
                "Untitled",
                false
            ),
            Song(
                listOf(
                    artist1,
                    artist2
                ),
                artist1,
                "Miss u bitch",
                false
            )
        )

        adapter.songs = songs_
        adapter.notifyDataSetChanged()

        super.onViewCreated(view, savedInstanceState)
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
}
