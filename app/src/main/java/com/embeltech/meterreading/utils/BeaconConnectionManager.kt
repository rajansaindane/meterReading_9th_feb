package com.embeltech.meterreading.utils

import com.polidea.rxandroidble2.RxBleConnection
import io.reactivex.Observable

/**
 * Used to keep Global connection Observable used to connect and configure beacon
 */
object BeaconConnectionManager {

    var connectionObservable: Observable<RxBleConnection>? = null
}