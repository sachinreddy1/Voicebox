package com.sachinreddy.feature.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sachinreddy.feature.viewModel.AppViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: DaggerAwareViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AppViewModel::class)
    abstract fun appVM(appViewModel: AppViewModel): ViewModel
}
