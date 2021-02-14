package com.sachinreddy.feature.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.sachinreddy.feature.fragment.HomeFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Provides Application dependencies into the Dependency Graph.
 */
@Module
abstract class FragmentModule {

    @Binds
    @PerActivity
    abstract fun bindFragmentFactory(factory: DefaultFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(HomeFragment::class)
    abstract fun provideHomeFragment(homeFragment: HomeFragment): Fragment
}
