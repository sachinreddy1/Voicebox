package com.sachinreddy.feature.di

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.sachinreddy.feature.MainActivity
import com.sachinreddy.feature.R
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Provides Application dependencies into the Dependency Graph.
 */
@Module(
    includes = [
        ViewModelModule::class,
        FragmentModule::class
    ]
)
abstract class MainActivityModule {

    @Binds
    abstract fun bindActivity(mainActivity: MainActivity): AppCompatActivity

    companion object {
        @Provides
        fun provideNavController(mainActivity: MainActivity): NavController =
            mainActivity.findNavController(R.id.app_nav_graph)
    }
}
