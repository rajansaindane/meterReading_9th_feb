
package com.embeltech.meterreading.injection.module

import android.content.Context
import com.embeltech.meterreading.config.Application
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.polidea.rxandroidble2.RxBleClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
open class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providesApplication(): Application = application

    @Provides
    @Singleton
    fun providesRxBleClient(): RxBleClient = RxBleClient.create(application)

    @Provides
    @Singleton
    fun providesAppPreferences(): AppPreferences = AppPreferences.getInstance(application)
}