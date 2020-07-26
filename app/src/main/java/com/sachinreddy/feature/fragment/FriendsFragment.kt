package com.sachinreddy.feature.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sachinreddy.feature.R
import com.sachinreddy.feature.adapter.ArtistAdapter
import com.sachinreddy.feature.data.Artist
import com.sachinreddy.feature.data.TestData
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
        val artists_: MutableList<Artist> =
            mutableListOf(TestData.artist1, TestData.artist2, TestData.artist3)
        adapter = ArtistAdapter(context!!, artists_)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        friends_recycler_view.adapter = adapter
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
        searchView.queryHint = "Search Artists"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.artistFilter.filter(newText)
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                validNavController?.navigate(R.id.action_FriendsFragment_to_HomeFragment)
            R.id.add_friends ->
                Snackbar.make(view!!, "Adding friend...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
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
