
package com.embeltech.meterreading.injection

import com.embeltech.meterreading.injection.annotations.ConfigPersistent
import com.embeltech.meterreading.injection.module.ActivityModule
import com.embeltech.meterreading.injection.module.FragmentModule
import com.embeltech.meterreading.injection.module.ViewModelModule
import dagger.Subcomponent

/**
 * A dagger component that will live during the lifecycle of an Activity but it won't
 * be destroy during configuration changes. Check [ConfigPersistentDelegate] to see how this components
 * survives configuration changes.
 * Use the [ConfigPersistent] scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters, ViewModels).
 */
@ConfigPersistent
@Subcomponent(modules = [ViewModelModule::class])
interface ConfigPersistentComponent {
    operator fun plus(activityModule: ActivityModule): ActivityComponent
    operator fun plus(fragmentModule: FragmentModule): FragmentComponent
}