package com.sachinreddy.feature.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sachinreddy.feature.viewModel.AppViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Reusable
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

/**
 * Declares ViewModels available for injection.
 *
 * This binds the specific types (`MyViewModel`) to the generic injection
 * binding (`ViewModel`) Set so that they can be retrieved by the
 * `InjectedViewModelFactory` class.
 */
@Module
abstract class ViewModelBindings {
    @Binds
    abstract fun factory(factory: InjectedViewModelFactory): ViewModelProvider.Factory

    @ViewModelKey(AppViewModel::class)
    @Binds
    @IntoMap
    abstract fun app(vm: AppViewModel): ViewModel
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

/**
 * An Injectable ViewModel Factory.
 *
 * This finds the ViewModel by class name in the DI graph.
 * Add ViewModels with `@IntoSet` to allow this class to find them.
 */
@Reusable
class InjectedViewModelFactory @Inject constructor(
    private val providers: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return providers[modelClass]?.get() as T
    }
}
