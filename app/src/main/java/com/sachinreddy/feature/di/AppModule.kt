package com.sachinreddy.feature.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Vibrator
import com.sachinreddy.feature.BaseApplication
import com.sachinreddy.feature.MainActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

/**
 * Provides Application dependencies into the Dependency Graph.
 */
@Module(includes = [AndroidSupportInjectionModule::class])
abstract class AppModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    abstract fun mainActivityInjector(): MainActivity

    @Binds
    abstract fun application(app: BaseApplication): Application

    @Binds
    abstract fun applicationContext(app: BaseApplication): Context

    companion object {
        @Provides
        fun resources(app: BaseApplication) = app.resources

        @Provides
        fun connectivityManager(app: BaseApplication) =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        @Provides
        fun vibrator(app: BaseApplication) =
            app.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
}
