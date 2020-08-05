package com.sachinreddy.feature.injection

import com.sachinreddy.feature.activity.AppActivity
import com.sachinreddy.feature.activity.AuthActivity
import com.sachinreddy.feature.fragment.LoginFragment
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
    fun inject(target: LoginFragment)
    fun inject(target: AuthActivity)
    fun inject(target: AppActivity)
}
