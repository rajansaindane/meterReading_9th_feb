
package com.embeltech.meterreading.ui

import androidx.lifecycle.ViewModel
import com.embeltech.meterreading.data.database.model.Beacon
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.livedata.SingleLiveEvent
import com.embeltech.meterreading.livedata.Status
import com.embeltech.meterreading.utils.BeaconConnectionManager
import com.embeltech.meterreading.utils.Utility
import com.polidea.rxandroidble2.RxBleConnection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.*
import javax.inject.Inject

open class BaseViewModel : ViewModel() {
    @Inject
    lateinit var appPref: AppPreferences

    var status = SingleLiveEvent<Status>()

    var disposable = CompositeDisposable()

    var connectionObservable: Observable<RxBleConnection>?
        get() = BeaconConnectionManager.connectionObservable
        set(value) {
            BeaconConnectionManager.connectionObservable = value
        }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    /**
     * Method parse the all byte arrays and convert them to respected Beacon object variables
     */
    fun getBeacon(
        byteArrayBeaconId: ByteArray,
        byteArrayBeaconType: ByteArray,
        byteArrayBeaconGeoFence: ByteArray?,
        byteArrayFirmware: ByteArray,
        byteArrayHardware: ByteArray,
        byteArrayBattery: ByteArray,
        byteArrayATDL: ByteArray
    ): Beacon {
        val beacon = Beacon()
        val beaconId = Utility.getHexToDecimal(Utility.getByteArrayToHexBeaconId(byteArrayBeaconId))
        val intBeaconType = Utility.getHexToDecimal(Utility.getByteArrayToHexBeaconId(byteArrayBeaconType))
        var geoFence = ""
        byteArrayBeaconGeoFence.let {
            geoFence = Utility.hexToAscii(Utility.byteArrayToHexString(it!!))
        }

        val firmware = Utility.hexToAscii(Utility.byteArrayToHexString(byteArrayFirmware))
        val hardware = Utility.hexToAscii(Utility.byteArrayToHexString(byteArrayHardware))
        val battery = Utility.getHexToDecimal(Utility.getByteArrayToHexBeaconId(byteArrayBattery))

        Timber.d("AuditTrail array is %s", Arrays.toString(byteArrayATDL))

        var diagnosticLogging = false
        val diaByte = byteArrayATDL[0]
        if (diaByte.toInt() == 1) {
            diagnosticLogging = true
        }

        var auditTrail = false
        val auditTrailByte = byteArrayATDL[1]
        if (auditTrailByte.toInt() == 1) {
            auditTrail = true
        }

        Timber.d("Beacon Firmware is $firmware")
        Timber.d("Beacon Hardware is $hardware")
        Timber.d("Beacon Battery is $battery")
        Timber.d("Dia Logging is $diagnosticLogging")
        Timber.d("AuditTrail is $auditTrail")
        /*Timber.d("encoded geo fence string is " + geoFence)

        Timber.d(
            "Polyline decoded lat longs are " + Arrays.deepToString(
                arrayOf(
                    PolylineEncoding.decode(
                        geoFence
                    )
                )
            )
        )*/

        /*Timber.d("Beacon Id array is %s", Arrays.toString(byteArrayBeaconId))
        Timber.d("Beacon Id is $beaconId")
        Timber.d("Beacon Type array is %s", Arrays.toString(byteArrayBeaconType))
        Timber.d("Beacon Type is $intBeaconType")*/

        beacon.beaconId = beaconId
        beacon.beaconType = intBeaconType
        beacon.geoFence = geoFence
        beacon.firmware = firmware
        beacon.hardware = hardware
        beacon.battery = battery
        beacon.logStatus = diagnosticLogging
        beacon.auditStatus = auditTrail

        return beacon
    }
}
