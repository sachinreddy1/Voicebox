package com.sachinreddy.feature.viewModel

import androidx.lifecycle.ViewModel
import com.sachinreddy.feature.data.Track
import javax.inject.Inject

class AppViewModel @Inject constructor() : ViewModel() {
    var mTrackList: MutableList<Track>? = mutableListOf()
}