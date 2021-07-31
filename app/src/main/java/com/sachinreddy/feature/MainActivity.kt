package com.sachinreddy.feature

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.sachinreddy.feature.databinding.ActivityMainBinding
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.fragmentFactory = fragmentFactory

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        test.text = Method()
    }

    external fun Method(): String

    companion object {
        init {
            System.loadLibrary("cpp_code")
        }
    }
}
