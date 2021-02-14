package com.sachinreddy.feature.di

import com.sachinreddy.feature.MainActivity
import com.sachinreddy.feature.fragment.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        ViewModelBindings::class
    ]
)
interface ApplicationComponent {
    fun inject(target: DependencyApp)
    fun inject(target: MainActivity)
    fun inject(target: HomeFragment)
}
