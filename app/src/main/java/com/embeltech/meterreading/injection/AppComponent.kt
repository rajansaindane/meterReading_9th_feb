
package com.embeltech.meterreading.injection

import com.embeltech.meterreading.data.repository.BIRepository
import com.embeltech.meterreading.injection.module.*
import com.embeltech.meterreading.injection.module.SchedulerModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, RepositoryModule::class, DatabaseModule::class,
    ApiServiceModule::class, SchedulerModule::class])
interface AppComponent {

    fun activityModule(activityModule: ActivityModule): ActivityComponent
    fun fragmentModule(fragmentModule: FragmentModule) : FragmentComponent

    fun provideRepository(): BIRepository
}