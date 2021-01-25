package com.sachinreddy.feature.injection

import com.sachinreddy.feature.activity.AppActivity
import com.sachinreddy.feature.fragment.HomeFragment
import com.sachinreddy.feature.modules.ApplicationModule
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
    fun inject(target: AppActivity)
    fun inject(target: HomeFragment)
}
