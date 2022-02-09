
package com.embeltech.meterreading.extensions

import android.net.ConnectivityManager

val ConnectivityManager.isConnected: Boolean
    get() = activeNetworkInfo?.isConnected ?: false