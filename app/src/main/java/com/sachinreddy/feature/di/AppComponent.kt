package com.sachinreddy.feature.di

import com.sachinreddy.feature.BaseApplication
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : AndroidInjector<BaseApplication> {
    @Component.Factory
    interface Factory : AndroidInjector.Factory<BaseApplication> {}
}
