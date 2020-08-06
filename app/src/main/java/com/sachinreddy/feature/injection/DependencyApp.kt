package com.sachinreddy.feature.injection

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.sachinreddy.feature.modules.ApplicationModule

@Suppress("Unused")
/**
 * Cleans up anything that needs cleaning up globally.
 */
class DependencyApp : Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }
}

/**
 * Get the Application Component for injection from the Application.
 */
val Activity.appComponent get() = (application as DependencyApp).component

/**
 * Get the Application Component for injection from the Application.
 */
val Fragment.appComponent get() = (activity?.application as DependencyApp?)?.component
