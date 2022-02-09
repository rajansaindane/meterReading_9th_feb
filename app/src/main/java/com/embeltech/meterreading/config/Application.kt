package com.embeltech.meterreading.config

import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.embeltech.meterreading.injection.AppComponent
import com.embeltech.meterreading.injection.DaggerAppComponent
import com.embeltech.meterreading.injection.module.*
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.internal.RxBleLog
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

class Application : MultiDexApplication(), LifecycleObserver {
    lateinit var appComponent: AppComponent

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        configureExceptionLogging()
        buildComponent()
        RxBleClient.setLogLevel(RxBleLog.DEBUG)
    }

    private fun configureExceptionLogging() {
        val default = Thread.getDefaultUncaughtExceptionHandler()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        RxJavaPlugins.setErrorHandler(Timber::e)
    }

    /**
     * Generating the Dagger Graph
     */
    private fun buildComponent() {
         appComponent = DaggerAppComponent.builder()
             .appModule(AppModule(this))
             .repositoryModule(RepositoryModule())
             .databaseModule(DatabaseModule())
             .apiServiceModule(ApiServiceModule())
             .schedulerModule(SchedulerModule())
             .viewModelModule(ViewModelModule())
             .build()
    }
}