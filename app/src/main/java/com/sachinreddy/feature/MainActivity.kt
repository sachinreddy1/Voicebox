package com.sachinreddy.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sachinreddy.feature.databinding.ActivityMainBinding
import com.sachinreddy.feature.di.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import javax.inject.Inject

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}
