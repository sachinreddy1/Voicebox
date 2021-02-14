package com.sachinreddy.feature.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Vibrator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Provides Application dependencies into the Dependency Graph.
 */
@Module
class ApplicationModule(private val application: Application) {
    @Provides
    @Singleton
    fun application() = application

    @Provides
    @Singleton
    fun context() = application.applicationContext

    @Provides
    @Singleton
    fun resources() = application.resources

    @Provides
    @Singleton
    fun connectivityManager() =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun vibrator() = application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
}
