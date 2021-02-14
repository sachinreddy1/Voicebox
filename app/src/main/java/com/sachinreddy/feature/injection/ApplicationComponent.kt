package com.sachinreddy.feature.injection

import com.sachinreddy.feature.activity.MainActivity
import com.sachinreddy.feature.fragment.HomeFragment
import com.sachinreddy.feature.modules.ApplicationModule
import com.sachinreddy.feature.modules.ViewModelBindings
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
