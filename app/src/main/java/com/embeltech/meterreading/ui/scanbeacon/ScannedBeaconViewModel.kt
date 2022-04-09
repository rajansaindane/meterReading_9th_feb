package com.embeltech.meterreading.ui.scanbeacon

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.config.Constants.Beacon.CONNECT_TIMEOUT
import com.embeltech.meterreading.config.Constants.Progress.PROGRESS_CONNECT_BEACONS_SUCCESS
import com.embeltech.meterreading.config.Constants.Progress.PROGRESS_CONNECT_BEACON_FAILED
import com.embeltech.meterreading.config.Constants.Progress.PROGRESS_CONNECT_BEACON_STARTED
import com.embeltech.meterreading.config.Constants.Progress.PROGRESS_NO_SCANNED_BEACONS_FOUND
import com.embeltech.meterreading.data.database.model.Beacon
import com.embeltech.meterreading.data.database.model.MeterBeacon
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.data.repository.BIRepository
import com.embeltech.meterreading.extensions.addToCompositeDisposable
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.BaseViewModel
import com.embeltech.meterreading.ui.billing.model.DeviceDataDetail
import com.embeltech.meterreading.ui.scanbeacon.model.BeaconPayload
import com.embeltech.meterreading.utils.Utility
import com.jakewharton.rx.ReplayingShare
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScannedBeaconViewModel @Inject constructor(private val repository: BIRepository) :
    BaseViewModel() {
    var scanResult: MutableLiveData<List<MeterBeacon>> = MutableLiveData()
    var connectedBeaconLiveData: MutableLiveData<Beacon> = MutableLiveData()
    private var rxBleDevice: RxBleDevice? = null

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var rxBleClient: RxBleClient
    private var connectionDisposable: Disposable? = null
    private var timerDisposable: Disposable? = null
    private var beaconList: ArrayList<MeterBeacon> =
        ArrayList() // Used to add scanned beacons and set to live data

    /**
     * Observer status value
     */
    fun getStatus(): LiveData<Status> {
        return status
    }

    /**
     * Establish Beacon connection and returns Connection Observable object
     */
    private fun prepareConnectionObservable(): Observable<RxBleConnection> {
        return rxBleDevice?.establishConnection(true)!!
            .observeOn(AndroidSchedulers.mainThread())
            .compose(ReplayingShare.instance())
    }

    /**
     * Method Scan Beacons with Service Filter so that only required Beacons were Scanned and displayed
     */
    @SuppressLint("BinaryOperationInTimber")
    fun scanBLE(scanBeaconActivity: ScanBeaconActivity) {
        var scannedList = false
        status.value = ShowProgressDialog(Constants.Progress.PROGRESS_SCANNING_BEACON_STARTED)
        rxBleClient.scanBleDevices(
            ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build()/*,
            ScanFilter.Builder()
                .setServiceUuid(ParcelUuid(UUID.fromString(Constants.Beacon.BEACON_SERVICE_FILTER)))
                .build()*/
        )
            .subscribeOn(Schedulers.io())
//            .take(Constants.Beacon.SCAN_TIMEOUT, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // Hide progress bar
                Utility.cancelLoading()
                status.value = ShowProgressDialog(Constants.Progress.PROGRESS_SCANNED_BEACONS_FOUND)

                val beacon = MeterBeacon()
                beacon.beaconName = "Beacon - ${it.bleDevice.name.toString()}"
                beacon.beaconMacId = it.bleDevice.macAddress
                beacon.byteArray = it.scanRecord.bytes
                beacon.batteryLevel = it.scanRecord.txPowerLevel
                beacon.rssi = it.rssi

                Log.e(
                    "Scan beacon VM : ",
                    "${it.bleDevice.macAddress} - ${it.bleDevice.name.toString()}"
                )

                var isNew = true
                for (b in beaconList) {
                    if (b.beaconMacId == beacon.beaconMacId) {
                        isNew = false
                        if (!(b.beaconName.equals(beacon.beaconName, true))) {
                            b.beaconName = beacon.beaconName
                        }
                        break
                    }
                }
                if (isNew) {
                    beaconList.add(beacon)
                    scanResult.value = beaconList
                }

                scannedList = true

            }, {
                Toast.makeText(scanBeaconActivity, "Total Beacon Devices Scan : "+beaconList.size, Toast.LENGTH_SHORT).show()
                Toast.makeText(scanBeaconActivity, "Scanning completed", Toast.LENGTH_SHORT).show()
                Utility.cancelLoading()
                it.printStackTrace()
                Timber.e("Scanned beacon exception is " + it.message)
                status.value = ShowProgressDialog(Constants.Progress.PROGRESS_SCANNED_BEACONS_FOUND)
                Timber.e("Scanned beacon exception is " + it.printStackTrace())
            }, {
                if (!scannedList)
                    status.value = ShowProgressDialog(PROGRESS_NO_SCANNED_BEACONS_FOUND)
            }).addToCompositeDisposable(disposable)
    }

    fun connectToBeacon(beacon: MeterBeacon) {
        val macAddress = beacon.beaconMacId
        rxBleDevice = rxBleClient.getBleDevice(macAddress)

        connectionObservable = prepareConnectionObservable()
        status.value = ShowProgressDialog(PROGRESS_CONNECT_BEACON_STARTED)

        timerDisposable = Completable.timer(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                status.value = ShowProgressDialog(PROGRESS_CONNECT_BEACON_STARTED)
            }
            .subscribe({
                status.postValue(ShowProgressDialog(PROGRESS_CONNECT_BEACONS_SUCCESS))
                insertBeaconInDB(beacon)
                if (rxBleDevice?.connectionState != RxBleConnection.RxBleConnectionState.CONNECTED) {
                    disconnectBeacon()
                    Log.e("Scan", "Check Beacon configuration mode!")
                    status.value =
                        ShowProgressDialog(PROGRESS_CONNECT_BEACON_FAILED)
                }
            }, {
                status.postValue(ShowProgressDialog(PROGRESS_CONNECT_BEACON_FAILED))
                it.printStackTrace()
                Log.e("Scan Error", it!!.message!!)
            })
    }

    private fun disconnectBeacon() {
        connectionDisposable?.dispose()
    }

    private fun insertBeaconInDB(meterBeacon: MeterBeacon) {
        val id = repository.insertBeacon(meterBeacon)
        if (id > 0) {
            status.value = DataSaveSuccessfully(id)
        }
    }

    fun getAllDevices() {
        repository.getAllDevices()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.i("@scan","local=====>"+it.toString())
                status.value = GetAllDeviceListFromDB(it)
            }
            .doOnError {
                Log.i("@scan","error local=====>"+it.toString())
                status.value = Failed(it.message!!)
            }
            .subscribe()
    }

    fun saveBeaconData(requests: BeaconPayload) {
        repository.saveBeaconData(appPreferences.getToken()!!, requests)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = SaveBeaconData(it.toString())
               Log.i("@status", "success ===>$it")

            }, {
                Log.i("@status", "fail ===>${it.message}")
                Log.i("@status", "fail ===>${it.toString()}")
                Log.i("@status", "fail ===>${appPreferences.getToken()!!}")

                status.value = Failed(it.message!!)
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
            }).addToCompositeDisposable(disposable)
    }


}