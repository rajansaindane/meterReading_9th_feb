package com.embeltech.meterreading.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.embeltech.meterreading.BuildConfig
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.BaseActivity
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.config.Constants.StatusMessages.LOG_EMPTY
import com.embeltech.meterreading.data.database.model.MeterBeacon
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.billing.model.DeviceDataDetail
import com.embeltech.meterreading.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.row_billing_device_details.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomeActivity : BaseActivity() {

    private var exportCount: Int = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var homeScreenViewModel: HomeScreenViewModel

    private var beacon: MeterBeacon? = null
    private var cal = Calendar.getInstance()
    private var selectedAMRId = ""
    private var selectedStartDate = ""
    private var selectedEndDate = ""
    private var isStartDateSelected: Boolean = false
    private var amrIdList: ArrayList<String> = ArrayList()
    private var deviceDataDetail = DeviceDataDetail()
    private var dataSampleCount: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(R.layout.activity_home)

        beacon = intent.getSerializableExtra("selected_beacon") as MeterBeacon?
        homeScreenViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(HomeScreenViewModel::class.java)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        homeScreenViewModel.getAllAmrIdList()
        startDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    startDate.text = simpleDateFormat.format(cal.time)
                    selectedStartDate = startDate.text.toString()
                    deviceDataDetail.startDate = selectedStartDate
                    /*billingViewModel.getTotalVolumeAgainstAMRId(
                        selectedStartDate,
                        selectedEndDate,
                        selectedAMRId
                    )*/
                },
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        amrIdSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    selectedAMRId = amrIdList[position]
                    homeScreenViewModel.getLastBillNumber(selectedAMRId)
                    homeScreenViewModel.getDataSampleCount(selectedAMRId)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        endDate.setOnClickListener {
            isStartDateSelected = false
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    endDate.text = simpleDateFormat.format(cal.time)
                    selectedEndDate = endDate.text.toString()
                    deviceDataDetail.endDate = selectedEndDate
                    homeScreenViewModel.getTotalVolumeAgainstAMRId(
                        selectedStartDate,
                        selectedEndDate,
                        selectedAMRId
                    )
                },
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        homeScreenViewModel.status.observe(this, {
            handleStatus(it)
        })
        updateUI(beacon)
        handleFabAction()

        getTotal.setOnClickListener {
            val pricePerLiter = pricePerLiter.text.toString().toDouble()
            if (pricePerLiter > 0) {
                deviceDataDetail.pricePerLiter = pricePerLiter
                val totalConsumptionReading = beacon!!.meterReading * pricePerLiter
                totalConsumptionAmount.text = "Rs. $totalConsumptionReading"
                deviceDataDetail.totalAmount = totalConsumptionReading
                val userDetails = deviceDataDetail.nameAndAddress.split(",")
                if (userDetails.isNotEmpty()) {
                    deviceDataDetail.billUserName = userDetails[0]
                    deviceDataDetail.billUserId = userDetails[4].toLong()
                    deviceDataDetail.billingAddress = userDetails[2]
                    deviceDataDetail.billingCity = userDetails[1]
                    deviceDataDetail.billingState = userDetails[3]
                }
                homeScreenViewModel.getBillingDetails(deviceDataDetail)
            }else{
                Toast.makeText(this, "Please enter price per liter", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Method used to fetch Logs from database and
     * export them in separate csv files
     */
    private fun handleFabAction() {
        fabExportLogs.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Check if build version is Marshmallow and above to ask for storage permission to scan nearby beacons
                if (!checkStoragePermission()) {
                    requestStoragePermission()
                } else {
                    exportLogs()
                }
            } else {
                exportLogs()
            }
        }
    }

    private fun exportLogs() {
        exportCount = 0
        homeScreenViewModel.exportData()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(beacon: MeterBeacon?) {
        connectedDeviceName.text = "Device Name : ${beacon!!.beaconName}"
        deviceDataDetail.totalConsumption = beacon.meterReading.toDouble()
    }

    @SuppressLint("SetTextI18n")
    private fun handleStatus(it: Status?) {
        exportCount++
        when (it) {
            is ExportStatus -> {
                if (exportCount == 2) {
                    try {
                        val uriList: ArrayList<Uri> = ArrayList()
                        homeScreenViewModel.diaLogFile?.let {
                            uriList.add(
                                FileProvider.getUriForFile(
                                    this@HomeActivity,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    it
                                )
                            )
                        }

                        // Checks if URI list is not empty before share operations
                        if (uriList.isNotEmpty()) {
                            val sharingIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
                            sharingIntent.type = "text/*"
                            sharingIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, uriList)
                            startActivity(
                                Intent.createChooser(
                                    sharingIntent,
                                    resources.getString(R.string.string_share_file)
                                )
                            )
                        } else {
                            // To no data found to export toast message
                            showToast(LOG_EMPTY)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            is GetAMRIdList -> {
                if (it.amrIds.isNotEmpty()) {
                    amrIdList = it.amrIds as ArrayList<String>
                    setDataToUI(amrIdList)
                } else {
                    DialogUtils.showOkAlert(this, "Meter Reading", "Data not found")
                }
            }
            is GetLastBillNumber -> {
                deviceDataDetail.billNumber = it.billNumber.toLong()
                deviceDataDetail.amrId = selectedAMRId
                updateDeviceData(deviceDataDetail)
            }
            is GetTotalConsumption -> {
                if (it.consumptionUnits[0] != null)
                    deviceDataDetail.totalConsumption =
                        it.consumptionUnits[0].toDouble()
                updateDeviceData(deviceDataDetail)
            }
            is GetUserNameAndAddress -> {
                deviceDataDetail.nameAndAddress = it.userNameAndAddress[0]
                updateDeviceData(deviceDataDetail)
            }
            is BillingDataSavedSuccessfully -> {
                getInvoice.visibility = View.VISIBLE
            }
            is GetDataSampleCount -> {
                dataSampleCount = it.dataSampleCount
                totalConsumption.text =
                    "Total Consumption : ${beacon!!.meterReading * dataSampleCount}"
            }
        }
    }

    private fun updateDeviceData(deviceDetailsList: DeviceDataDetail) {
        val nameAddress = deviceDetailsList.nameAndAddress.split(",")
        if (nameAddress.isNotEmpty() && nameAddress.size > 2) {
            val name = "${nameAddress[0]}, ${nameAddress[1]}, ${nameAddress[2]}, ${nameAddress[3]}"
            nameAndAddress.text = name
        }
        amrId.text = "AMR ID : $selectedAMRId"
        billNumber.text = "Bill Number : ${deviceDetailsList.billNumber}"
//        totalConsumption.text = "Total Consumption : ${deviceDetailsList.totalConsumption}"
    }

    private fun setDataToUI(amrIds: ArrayList<String>) {
        amrIds.add(0, "Select AMR Id")
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, amrIds)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        amrIdSpinner.adapter = adapter
    }

    /**
     * Android method called after user processed runtime permission requested by application
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.d("HomeActivity In onRequestPermissionsResult")

        when (requestCode) {
            Constants.Permission.STORAGE_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    exportLogs()
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) run {
                        showStoragePermissionRequiredDialog()
                    } else {
                        DialogUtils.showSnackBarMsg(
                            this,
                            getCoordinatorLayout(),
                            resources.getString(R.string.string_storage_permission),
                            Snackbar.LENGTH_LONG,
                            resources.getString(R.string.enable)
                        ) {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    /**
     * Show dialog to user describing why storage permission required before export process
     */
    private fun showStoragePermissionRequiredDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(resources.getString(R.string.string_storage_permission))
            .setTitle(resources.getString(R.string.string_permission_title))
        builder.setPositiveButton(
            resources.getString(R.string.string_btn_ok)
        ) { _, _ ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    Constants.Permission.STORAGE_PERMISSION_REQUEST
                )
            }
        }
        builder.show()
    }
}