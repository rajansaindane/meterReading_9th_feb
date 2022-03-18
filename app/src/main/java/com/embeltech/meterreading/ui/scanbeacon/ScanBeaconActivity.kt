package com.embeltech.meterreading.ui.scanbeacon

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.BaseActivity
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.config.Constants.Permission.GPS_ENABLE_REQUEST
import com.embeltech.meterreading.data.database.model.MeterBeacon
import com.embeltech.meterreading.eventbus.RxBus
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.device.model.Device
import com.embeltech.meterreading.ui.home.HomeActivity
import com.embeltech.meterreading.ui.scanbeacon.model.BeaconPayload
import com.embeltech.meterreading.utils.DialogUtils
import com.embeltech.meterreading.utils.Utility
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_scan_beacon.*
import kotlinx.android.synthetic.main.content_scan_beacon.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ScanBeaconActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var scannedBeaconViewModel: ScannedBeaconViewModel

    private var snackbar: Snackbar? = null
    private lateinit var selectedBeacon: MeterBeacon
    private var deviceList: List<Device> = ArrayList()
    private val requests : List<BeaconPayload> = ArrayList()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(R.layout.activity_scan_beacon)
        setSupportActionBar(findViewById(R.id.toolbar))

        scanBeaconRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        scannedBeaconViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ScannedBeaconViewModel::class.java)

        scannedBeaconViewModel.getAllDevices()

        scannedBeaconViewModel.getStatus().observe(this, {
            handleStatus(it)
        })

        scannedBeaconViewModel.scanResult.observe(this, {
            setBeaconListToUI(it)
        })
        registerBeaconSelectedObserver()
        swipeRefreshLayout.setOnRefreshListener {
            Utility.showLoadingDialog(this,"Scanning ...")
            scannedBeaconViewModel.scanBLE(this)
        }

        fab.setOnClickListener {
            scannedBeaconViewModel.scanBLE(this)
        }
        checkPermissions()
        scannedBeaconViewModel.scanBLE(this)
        //checkRecord()
    }

    fun checkRecord(){
        val hex =  "0201041AFF59000215454D422D310100DE60B464000020001D240000F604000000000000000000000000"
        Log.i("@scan","data===>"+getConvertedString(hex))
        //0201041AFF5 9 0 0 0 2 1 5 4 5 4 D 4 2 2 D 3 1 0 1 0 0 6 0 B 4 6 4 0 0 0 0 2 0 0 0 1 D 2 4 0 0 0 0 F 6 0 4
        //0123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657
        val convertedString = getConvertedString(hex)
        Log.i("@scan","sub string===>"+getConvertedString(hex).substring(0, 10))
        val beaconName = Utility.getBeaconName(convertedString)
        //val beaconMacId = it.beaconMacId
        val meterReading = Utility.getMeterReading(convertedString)
        val serialNumber = Utility.getSerialNumber(convertedString)
        val battery = Utility.getBatteryPercentage(convertedString)
        //val rssi = it.rssi
        Log.i("@scan","received data===>"+beaconName+" "
                +" "+meterReading+" "+serialNumber+" "+battery);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerBeaconSelectedObserver() {
      //  Utility.showLoadingDialog(this,"Scanning ...")
        Log.i("@scan","service called===>")
        RxBus.subscribe(RxBus.SUBJECT_SCANNED_BEACON_SELECTED, this) {
            Log.i("@scan","becon selected===>")
            if (it is MeterBeacon) {
                val stringBuilder = StringBuilder()
                Log.i("@scan","receiving===>")

                val hex = Utility.byteArrayToHexString(it.byteArray!!)
              //  val hex =  "0201041AFF59000215454D422D310100DE60B464000020001D240000F604000000000000000000000000"
                Log.i("@scan","data===>"+getConvertedString(hex))
                //0201041AFF5 9 0 0 0 2 1 5 4 5 4 D 4 2 2 D 3 1 0 1 0 0 6 0 B 4 6 4 0 0 0 0 2 0 0 0 1 D 2 4 0 0 0 0 F 6 0 4
                //0123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657
                val convertedString = getConvertedString(hex)
                val beaconName = Utility.getBeaconName(convertedString)
                val beaconMacId = it.beaconMacId
                val meterReading = Utility.getMeterReading(convertedString)
                val serialNumber = Utility.getSerialNumber(convertedString)
                val battery = Utility.getBatteryPercentage(convertedString)
                val rssi = it.rssi
                Log.i("@scan","received data===>"+beaconName+" "+beaconMacId
                +" "+meterReading+" "+serialNumber+" "+battery+" "+rssi);

                stringBuilder.append("\nBeacon Name : ").append(beaconName).append("\n\n")
                stringBuilder.append("Mac Address : " + it.beaconMacId).append("\n\n")
                stringBuilder.append("Meter Readings : ").append(meterReading).append("\n\n")
                stringBuilder.append("Serial Number : ").append(serialNumber).append("\n\n")
                stringBuilder.append("Battery Percentage : ").append(battery).append("%\n\n")
                stringBuilder.append("RSSI : " + it.rssi).append("\n\n")

//                DialogUtils.showOkAlert(this, "Beacon", stringBuilder.toString())

                val meterBeacon = MeterBeacon()
                meterBeacon.beaconName = beaconName
                meterBeacon.beaconMacId = beaconMacId
                meterBeacon.batteryLevel = battery
                meterBeacon.meterReading = meterReading.toLong()
                meterBeacon.rssi = rssi
                meterBeacon.serialNumber = serialNumber.toString()
                selectedBeacon = meterBeacon
                scannedBeaconViewModel.connectToBeacon(meterBeacon)
                saveBeaconData(meterBeacon,"hex","convertedString")
                Log.i("@scan","set data===>"+Gson().toJson(meterBeacon))

          }
            else{
                Log.i("@scan","else condition====>"+it.toString())
            }
           // hideProgressDialog()
          }


   }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveBeaconData(
        meterBeacon: MeterBeacon,
        hex: String,
        convertedString: String
    ) {
            val request = BeaconPayload()
            val requests : MutableList<BeaconPayload> =ArrayList()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val timeFormat = SimpleDateFormat("HH.mm.ss")
            request.beaconNname = meterBeacon.beaconName
            request.macAddress = meterBeacon.beaconMacId
            request.meterReading = meterBeacon.meterReading.toString()
            request.serialNumber = meterBeacon.serialNumber
            request.pluseCount = meterBeacon.meterReading.toString()
            request.rssi = meterBeacon.rssi.toString()
            request.batteryPercentage = meterBeacon.batteryLevel.toString()
            request.date = dateFormat.format(Date())
            request.time = timeFormat.format(Date()).replace(".","")

//        request.beaconNname = "Beacon_13"
//        request.macAddress = "12345"
//        request.meterReading = "1211"
//        request.serialNumber = "S124"
//        request.pluseCount = "24822"
//        request.rssi = "RSSI_TEST1"
//        request.batteryPercentage = "100"
//        request.date = dateFormat.format(Date())
//        request.time = timeFormat.format(Date()).replace(".","")
        Log.i("@Scan", "BeaconData====>"+Gson().toJson(request))
        requests.add(request)
        scannedBeaconViewModel.saveBeaconData(requests)

    }

    private fun setBeaconListToUI(it: List<MeterBeacon>?) {
////        val beaconList: ArrayList<MeterBeacon> = ArrayList()
////        Log.i("@Scan","meterbecon=====>"+it.toString())
////        if (it!!.isNotEmpty()) {
////            if (deviceList.isNotEmpty()) {
//                Log.i("@Scan","deviceList=====>"+deviceList.toString())
//                for (i in it.indices)
//                    for (j in deviceList.indices) {
//                        if (it[i].beaconMacId.equals(deviceList[j].deviceMACId, false)) {
//                            beaconList.add(it[i])
//                        }
//                    }
//            }
//        }
//        if (beaconList.isNotEmpty())
            scanBeaconRecyclerView.adapter = ScannedBeaconAdapter(this, it)
    }

    private fun handleStatus(it: Status?) {
        when (it) {
            is ShowProgressDialog -> {
                when (it.message) {
                    Constants.Progress.PROGRESS_SCANNED_BEACONS_FOUND -> {
                        swipeRefreshLayout.isRefreshing = false
                    }
                    Constants.Progress.PROGRESS_SCANNING_BEACON_STARTED -> {
                        swipeRefreshLayout.isRefreshing = true
                    }
                    Constants.Progress.PROGRESS_NO_SCANNED_BEACONS_FOUND -> {
                        swipeRefreshLayout.isRefreshing = false
                        DialogUtils.showOkAlert(this, "Beacon", "No beacon found.")
                    }
                    Constants.Progress.PROGRESS_CONNECT_BEACON_STARTED -> {
                        showProgressDialog()
                    }
                    Constants.Progress.PROGRESS_CONNECT_BEACON_FAILED -> {
                        hideProgressDialog()
                        DialogUtils.showSnackBarMsg(
                            this,
                            linearScannedBeacons,
                            "Connection to beacon failed.",
                            Snackbar.LENGTH_LONG
                        )
                    }
                    Constants.Progress.PROGRESS_CONNECT_BEACONS_SUCCESS -> {
                        hideProgressDialog()
                        DialogUtils.showSnackBarMsg(
                            this,
                            linearScannedBeacons,
                            "Connection Successful.",
                            Snackbar.LENGTH_LONG
                        )
                        gotoHomeScreen()
                    }
                }
            }
            is DataSaveSuccessfully -> {
                Toast.makeText(this, "Data saved successfully.", Toast.LENGTH_LONG).show()
            }
            is GetAllDeviceListFromDB -> {
                deviceList = it.devices
                Log.i("@Scan", "local device list ===>$deviceList")
            }
            is SaveBeaconData ->{
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun gotoHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("selected_beacon", selectedBeacon)
        startActivity(intent)
    }

    /**
     * Method check runtime permission like bluetooth service on or off before start beacons scanning
     */
    private fun checkPermissions() {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter === null) {
//            showToast(resources.getString(R.string.bluetooth_not_supported))
            DialogUtils.showSnackBarMsg(
                this,
                getCoordinatorLayout(),
                resources.getString(R.string.bluetooth_not_supported),
                Snackbar.LENGTH_SHORT
            )
        } else {
            if (!mBluetoothAdapter.isEnabled) {
                requestBluetoothService()
            } else {
                checkAPIVersion()
            }
        }
    }

    /**
     * Method used to request to enable Bluetooth service to start scanning process
     */
    private fun requestBluetoothService() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, Constants.Permission.REQUEST_ENABLE_BT)
    }

    /**
     * Method checks for android OS version so that runtime permissions can be asked befor start beacon scanning
     */
    private fun checkAPIVersion() {
        if (checkBleSupport()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Check if build version is Marshmallow and above to ask for location permission to scan nearby beacons
                if (!checkLocationPermission()) {
                    requestLocationPermission()
                } else {
                    if (checkLocationSettings())
                        scannedBeaconViewModel.scanBLE(this)
                }
            } else {
                scannedBeaconViewModel.scanBLE(this)
            }
        } else {
            DialogUtils.showSnackBarMsg(
                this,
                getCoordinatorLayout(),
                resources.getString(R.string.ble_not_supported),
                Snackbar.LENGTH_SHORT
            )
        }
    }

    /**
     * Shows Snackbar describing app requires Location service to Scan Beacons
     */
    private fun showLocationServiceRequiredMsg() {
        DialogUtils.showSnackBarMsg(
            this,
            getCoordinatorLayout(),
            resources.getString(R.string.location_service_required),
            Snackbar.LENGTH_LONG,
            resources.getString(R.string.enable)
        ) {
            //fireLocationServiceIntent()
            displayLocationSettingsRequest(this, this)
        }
    }

    /**
     * Method will be invoked after user process bluetooth enable service request
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("Scanned Beacon activity In onActivityResult")
        Timber.d("in onActivityResult $requestCode and $resultCode")

        when (requestCode) {
            Constants.Permission.REQUEST_ENABLE_BT -> {
                if (resultCode == Activity.RESULT_OK) {
                    checkAPIVersion()
                } else {
                    snackbar = Snackbar.make(
                        linearScannedBeacons,
                        resources.getString(R.string.string_bluetooth_permission),
                        Snackbar.LENGTH_INDEFINITE
                    )
                    snackbar!!.setAction(resources.getString(R.string.string_btn_ok)) {
                        requestBluetoothService()
                    }
                    snackbar!!.show()
                }
            }
            GPS_ENABLE_REQUEST -> if (resultCode == RESULT_OK || Utility.isLocationServiceEnabled(this.applicationContext)
            ) {
            } else {
                DialogUtils.showSnackBarMsg(
                    this,
                    getCoordinatorLayout(),
                    resources.getString(R.string.location_enabled_failed),
                    Snackbar.LENGTH_LONG,
                    resources.getString(R.string.enable)
                ) {
                    displayLocationSettingsRequest(this, this)
                }
            }
        }
    }

    private fun getConvertedString(hex: String): String {
        var newString = hex.removeRange(0, 6)
        val length = newString.substring(0, 2)
        newString = newString.removeRange(0, 2)
        val dec = Utility.getHexToDecimal(length)
        newString = newString.substring(0, (dec * 2))

        newString = newString.removeRange(0, 8)
        val length1 = newString.substring(0, 2)
        newString = newString.removeRange(0, 2)
        val deci = Utility.getHexToDecimal(length1)
        newString = newString.substring(0, (deci * 2))

        return newString
    }
}