
package com.embeltech.meterreading.injection.annotations

import com.embeltech.meterreading.schedulers.SchedulerType
import javax.inject.Qualifier

/**
 * Qualifier to define Scheduler type (io, computation, or ui main thread).
 */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class RunOn(val value: SchedulerType = SchedulerType.IO)