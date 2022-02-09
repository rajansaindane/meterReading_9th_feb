/*
 * SchedulerModule.kt
 * Copyright (c) 2019. Intellore Systems Private Limited, Pune, India. All rights reserved.
 * Created by Sanjay.Sah
 */

package com.embeltech.meterreading.injection.module

import com.embeltech.meterreading.injection.annotations.RunOn
import com.embeltech.meterreading.schedulers.SchedulerType
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class SchedulerModule {

    @Provides
    @RunOn(SchedulerType.IO)
    internal fun provideIo(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @RunOn(SchedulerType.COMPUTATION)
    internal fun provideComputation(): Scheduler {
        return Schedulers.computation()
    }

    @Provides
    @RunOn(SchedulerType.UI)
    internal fun provideUi(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}