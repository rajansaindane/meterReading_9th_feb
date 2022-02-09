
package com.embeltech.meterreading.extensions
import android.content.Context
import com.embeltech.meterreading.config.Application

val Context.appComponent
    get() = (applicationContext as Application).appComponent
