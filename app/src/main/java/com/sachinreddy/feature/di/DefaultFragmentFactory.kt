package com.sachinreddy.feature.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject
import javax.inject.Provider

class DefaultFragmentFactory @Inject constructor(
    private val creator: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentClass =
            loadFragmentClass(
                classLoader,
                className
            )
        return creator[fragmentClass]?.get() ?: super.instantiate(classLoader, className)
    }
}