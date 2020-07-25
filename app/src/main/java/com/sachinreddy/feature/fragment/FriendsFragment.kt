package com.sachinreddy.feature.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.sachinreddy.feature.R
import com.sachinreddy.feature.adapter.ArtistAdapter
import com.sachinreddy.feature.data.Artist
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.fragment_friends.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FriendsFragment : Fragment() {
    lateinit var adapter: ArtistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        adapter = ArtistAdapter(requireContext())
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ArtistAdapter(context!!)
        friends_recycler_view.adapter = adapter

        val artist1 = Artist(
            "",
            "Artist Name",
            "test1@test1.com",
            "",
            "https://firebasestorage.googleapis.com/v0/b/collab-c4a6e.appspot.com/o/artists%2FcQcGlyqMi5SszPN4dBKhjR3WgG63?alt=media&token=99c3c99b-7485-43df-a0ec-4974cae1a227"
        )
        val artist2 = Artist(
            "",
            "Travis Scott",
            "travisscott@gmail.com",
            "",
            "https://i1.sndcdn.com/avatars-000701366305-hu9f0i-t500x500.jpg"
        )
        val artist3 = Artist(
            "",
            "Future",
            "future@gmail.com",
            "",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQup0zJL8qAuVwcDDwk_k_qerMYxldb7cUeWw&usqp=CAU"
        )

        val artists_: List<Artist> = listOf(artist1, artist2, artist3)
        adapter.artists = artists_
        adapter.notifyDataSetChanged()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        setupActionBar()
        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_friends, menu)

        val manager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search_friends)
        val searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(activity?.componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                Toast.makeText(requireContext(), "$query", Toast.LENGTH_LONG).show()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                validNavController?.navigate(R.id.action_FriendsFragment_to_HomeFragment)
        }
        return true
    }

    private fun setupActionBar() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(app_action_bar)
            supportActionBar?.apply {
                title = getString(R.string.artists)
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            }
        }
    }

    private val validNavController get() = findNavController().takeIf { it.valid }
    private val NavController.valid get() = currentDestination?.id == R.id.FriendsFragment
}
