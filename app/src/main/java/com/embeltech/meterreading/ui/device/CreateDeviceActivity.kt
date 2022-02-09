package com.embeltech.meterreading.ui.device

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.BaseActivity
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.extensions.hideSoftKey
import com.embeltech.meterreading.extensions.isEmpty
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.device.model.Admin
import com.embeltech.meterreading.ui.device.model.Device
import com.embeltech.meterreading.ui.device.model.DeviceListResponse
import com.embeltech.meterreading.ui.device.model.User
import com.embeltech.meterreading.utils.DialogUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_device.*
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CreateDeviceActivity : BaseActivity() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    lateinit var createDeviceViewModel: CreateDeviceViewModel

    private var allUserList: List<User> = ArrayList()
    private var allAdminList: List<Admin> = ArrayList()
    private var deviceData: DeviceListResponse? = null

    private var timezones: Array<String>? = null
    private var isEdit: Boolean = false
    private var isUpdate: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(R.layout.activity_create_device)
        back.setOnClickListener {
            onBackPressed()
            intent.putExtra("result", isUpdate)
        }

        if (intent != null) {
            isEdit = intent.getBooleanExtra("is_edit", false)
            if (isEdit) {
                deviceData = intent.getSerializableExtra("selected_device") as DeviceListResponse
                createDevice.text = "Update Device"
            } else {
                createDevice.text = "Create Device"
            }
        }

        createDeviceViewModel =
            ViewModelProviders.of(this, viewModelProvider).get(CreateDeviceViewModel::class.java)

        createDeviceViewModel.getStatus().observe(this, {
            handleStatus(it)
        })

        val role = appPreferences.getUserRole()
        if (role.equals("super-admin") || role.equals("admin")) {
            createDevice.visibility = View.VISIBLE
            deleteDevice.visibility = View.VISIBLE

        } else if (role.equals("user")) {
            createDevice.visibility = View.GONE
            deleteDevice.visibility = View.GONE
        }

        deleteDevice.setOnClickListener {
            if (deviceData != null)
                createDeviceViewModel.deleteDevice(deviceData!!.pkDeviceDetails)
        }

        createDeviceViewModel.getUserList()
        createDeviceViewModel.getAdminList()

        timezones = TimeZone.getAvailableIDs()
        setTimeZonesToSpinner(timezones)

        wakeTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mCurrentTime = Calendar.getInstance()
            val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
            val minutes = mCurrentTime.get(Calendar.MINUTE)
            val seconds = mCurrentTime.get(Calendar.SECOND)

            mTimePicker = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    var mHour = hourOfDay
                    var mSeconds = ""
                    val amPm: String
                    if (hourOfDay < 12) amPm = "AM"
                    else {
                        amPm = "PM"
                        mHour -= 12
                    }
                    mSeconds = if (seconds.toString().length == 1) {
                        "0$seconds"
                    } else {
                        seconds.toString()
                    }

                    wakeTime.setText("$mHour:$minute:$mSeconds $amPm")
                }, hour, minutes, false
            )
            mTimePicker.show()
        }

        amrEnable.setOnCheckedChangeListener { _, isChecked ->
            tamperEnable.isChecked = !isChecked
        }
        tamperEnable.setOnCheckedChangeListener { _, isChecked ->
            amrEnable.isChecked = !isChecked
        }
        deviceType.setSelection(1);
        createDevice.setOnClickListener {
            try {
                imei.hideSoftKey()

                val sMacId = macId.text.toString()
                val sSerialNo = serialNo.text.toString()
                val sDeviceName = deviceName.text.toString()

                var sImei = imei.text.toString()
                var sAmrId = amrid.text.toString()
                var sSim = sim.text.toString()
                var sImsi = imsi.text.toString()
                var time = wakeTime.text.toString()
                var dataSampleCount = dataSample.text.toString()
                var sLiterPerPlus = litrePerPulse.text.toString()
                val enteredState = state.text.toString()
                val sCity = city.text.toString()
                var sCustomerDeviceAddress = customerDeviceAddress.text.toString()
                var sCustomerName = customerName.text.toString()
                var sCustomerDeviceLocation = customerDeviceLocation.text.toString()
                var buildingName = wingOrBuilding.text.toString()
                var sPricePerLiter = pricePerLiter.text.toString()
                var sArea = area.text.toString()
                var sZone = zone.text.toString()
                var sMeterStartReading = meterStartReading.text.toString()

                val selectedTimeZone = timezone.selectedItem as String
                val selectedApplicationType = applicationOfAmr.selectedItem as String
                var selectedDiameter = diameterSize.selectedItem as String
                val selectedUser = user.selectedItem as String
                val selectedAdmin = admin.selectedItem as String
                val selectedUserPosition = user.selectedItemPosition
                val selectedAdminPosition = admin.selectedItemPosition
                var selectedType = type.selectedItem as String
                var selectedDeviceType = deviceType.selectedItem as String

                if (selectedDeviceType.equals("Select Device Type")) {
                    selectedDeviceType = "BLE"
                }

                if (selectedType.equals("Select option")) {
                    selectedType = "N/A"
                }

                var isAmrEnable = 0
                if (amrEnable.isChecked) isAmrEnable = 0
                else if (tamperEnable.isChecked) isAmrEnable = 1

                if (dataSampleCount.isEmpty()) {
                    dataSampleCount = "0"
                }
                if (sPricePerLiter.isEmpty()) {
                    sPricePerLiter = "0.0"
                }
                if (sLiterPerPlus.isEmpty()) {
                    sLiterPerPlus = "0.0"
                }
                if (sMeterStartReading.isEmpty()) {
                    sMeterStartReading = "0.0"
                }
                if (selectedDiameter.equals("Select size")) {
                    selectedDiameter = "0"
                }

                val na = "N/A"
                if (sImei.isEmpty()) sImei = na
                if (sAmrId.isEmpty()) sAmrId = na
                if (sSim.isEmpty()) sSim = na
                if (sImsi.isEmpty()) sImsi = na
                if (time.isEmpty()) time = na
                if (sCustomerName.isEmpty()) sCustomerName = na
                if (sCustomerDeviceAddress.isEmpty()) sCustomerDeviceAddress = na
                if (sCustomerDeviceLocation.isEmpty()) sCustomerDeviceLocation = na
                if (buildingName.isEmpty()) buildingName = na
                if (sArea.isEmpty()) sArea = na
                if (sZone.isEmpty()) sZone = na
                if (sMacId.isNotEmpty()) {
                    Log.i("@create", "===>$sMacId")
                    macId.error = null
                    if (sSerialNo.isNotEmpty()) {
                        serialNo.error = null
                        if (sDeviceName.isNotEmpty()) {
                            deviceName.error = null
                            if (timezone.selectedItemId > 0) {
                                val device = Device()
                                device.pkDeviceDetails = if (isEdit) deviceData!!.pkDeviceDetails
                                else 0
//                                device.deviceAdmin = selectedAdmin
//                                device.deviceAmrEnable = isAmrEnable.toLong()
                                 device.deviceAmrId = sAmrId
                                device.deviceApplicationOfAmr = selectedApplicationType
                                device.deviceArea = sArea
                                device.deviceAxisEnable = 0.toLong()
                                device.deviceBuildingNameOrWingName = buildingName
                                device.deviceCity = sCity
                                device.deviceCustomerAddress = sCustomerDeviceAddress
                                device.deviceCustomerName = sCustomerName
                                device.deviceDataSampleCount = dataSampleCount.toLong()
                                device.deviceDateTime = LocalDateTime.now().toString()
                                device.deviceDiameterSize = selectedDiameter.replace("mm","").trim().toLong()
                                device.deviceImei = sImei
                                device.deviceImsi = sImsi
                                device.deviceLitPerPrice = sPricePerLiter.toDouble()
                                device.deviceLiterPerPulse = sLiterPerPlus.toDouble()
                                 device.deviceMACId = sMacId
//                                device.deviceName = sDeviceName
//                                device.deviceMeterLocation = sCustomerDeviceLocation
//                                device.deviceMeterStartReading = sMeterStartReading.toDouble()
                                 device.deviceSerialNumber = sSerialNo
                                device.deviceSim = sSim
                                device.deviceState = enteredState
                                device.deviceTimeZone = selectedTimeZone
                                device.deviceUser = selectedUser
                                device.deviceType = selectedType
                                device.deviceTypeAmrOrBle =
                                    selectedDeviceType.toLowerCase(Locale.getDefault())
                                device.deviceWakeupTime = time
                                device.deviceZone = sZone
                                Log.i("@device","device=====>"+Gson().toJson(device.toString()))

                                if (admin.selectedItemId > 0) {
                                    device.deviceFkAdminId =
                                        allAdminList[selectedAdminPosition - 1].fkAdminId
                                } else {
                                    device.deviceFkAdminId = 0
                                }

                                if (user.selectedItemId > 0) {
                                    device.deviceFkUserId =
                                        allUserList[selectedUserPosition - 1].fkUserId
                                } else {
                                    device.deviceFkUserId = 0
                                }
                                if (!isEdit){
                                    Log.i("@create","device=====>"+Gson().toJson(device.toString()))
                                createDeviceViewModel.createDeviceRequest(device)}
                                else
                                    createDeviceViewModel.updateDeviceRequest(device)
                            } else {
                                Toast.makeText(this, "Please select Timezone", Toast.LENGTH_LONG)
                                    .show()
                            }
                        } else {
                            deviceName.error = "required"
                        }
                    } else {
                        serialNo.error = "required"
                    }
                } else {
                    Log.i("@create", "empty===>$sMacId")
                    macId.error = "required"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
        if (deviceData != null)
            updateFields(deviceData!!)
    }

    private fun updateFields(deviceData: DeviceListResponse) {
        imei.setText(deviceData.deviceImei)
        sim.setText(deviceData.deviceSim)
        imsi.setText(deviceData.deviceImsi)
        amrid.setText(deviceData.deviceAmrId)

        val amrEnableFlag = deviceData.deviceAmrEnable
        if (amrEnableFlag == 0.toLong()) amrEnable.isChecked = true
        else tamperEnable.isChecked = true
        wakeTime.setText(deviceData.deviceWakeupTime)

        dataSample.setText(deviceData.deviceDataSampleCount.toString())
        litrePerPulse.setText(deviceData.deviceLiterPerPulse.toString())

        customerName.setText(deviceData.deviceCustomerName)
        customerDeviceLocation.setText(deviceData.deviceMeterLocation)
        customerDeviceAddress.setText(deviceData.deviceCustomerAddress)

        pricePerLiter.setText(deviceData.deviceLitPerPrice.toString())
        meterStartReading.setText(deviceData.deviceMeterStartReading.toString())

        wingOrBuilding.setText(deviceData.deviceBuildingNameOrWingName)
        zone.setText(deviceData.deviceZone)
        area.setText(deviceData.deviceArea)

        macId.setText(deviceData.deviceMACId)
        serialNo.setText(deviceData.deviceSerialNumber)
        deviceName.setText(deviceData.deviceName)

        val selectedTimeZone = deviceData.deviceTimeZone
        val selectedApplicationOfAMR = deviceData.deviceApplicationOfAmr
        val selectedUser = deviceData.deviceUser
        val selectedAdmin = deviceData.deviceAdmin
        val selectedDiameterSize = deviceData.deviceDiameterSize
        val selectedDeviceType = deviceData.deviceTypeAmrOrBle
        val selectedType = deviceData.deviceType

        if (timezones != null && timezones!!.isNotEmpty()) {
            for (i in timezones!!.indices) {
                if (selectedTimeZone.equals(timezones!![i])) {
                    timezone.setSelection(i)
                }
            }
        }
        when (selectedApplicationOfAMR) {
            "Domestic" -> applicationOfAmr.setSelection(1)
            "Commercial" -> applicationOfAmr.setSelection(2)
            "STP" -> applicationOfAmr.setSelection(3)
            "ETP" -> applicationOfAmr.setSelection(4)
            else -> applicationOfAmr.setSelection(0)
        }

        /*when (selectedDiameterSize) {
            15.toLong() -> applicationOfAmr.setSelection(1)
            20.toLong() -> applicationOfAmr.setSelection(2)
            25.toLong() -> applicationOfAmr.setSelection(3)
            50.toLong() -> applicationOfAmr.setSelection(4)
            65.toLong() -> applicationOfAmr.setSelection(5)
            80.toLong() -> applicationOfAmr.setSelection(6)
            100.toLong() -> applicationOfAmr.setSelection(7)
            150.toLong() -> applicationOfAmr.setSelection(8)
            250.toLong() -> applicationOfAmr.setSelection(9)
            300.toLong() -> applicationOfAmr.setSelection(10)
            else -> applicationOfAmr.setSelection(0)
        }*/

        when (selectedDeviceType) {
//            "AMR", "amr" -> deviceType.setSelection(1)
            "BLE", "ble" -> deviceType.setSelection(1)
//            "LoraWan", "lorawan" -> deviceType.setSelection(3)
            else -> deviceType.setSelection(0)
        }

        when (selectedType) {
            "Volumetric" -> type.setSelection(1)
            "Mutlijet" -> type.setSelection(2)
            "Singlejet" -> type.setSelection(3)
            "Bulk" -> type.setSelection(4)
            "Electromagnetic" -> type.setSelection(5)
            "Ultrasonic" -> type.setSelection(6)
            else -> type.setSelection(0)
        }
    }

    private fun handleStatus(it: Status?) {
        when (it) {
            is ShowProgressDialog -> {
                when (it.message) {
                    Constants.Progress.SHOW_PROGRESS -> showProgressDialog()
                    Constants.Progress.HIDE_PROGRESS -> hideProgressDialog()
                }
            }
            is GetUserList -> updateView(it.users)
            is GetAdminList -> updateAdmin(it.admins)
            is DeviceSavedSuccessfully -> {
                val msg: String = if (!isEdit) "Device Saved Successfully."
                else "Device Updated Successfully."
                showDialogMessage(msg)
                clearAllData()
            }
            is DeviceDeletedSuccessfully -> {
                clearAllData()
                isUpdate = true
                showDialogMessage("Device Delete Successfully.")
            }
            is Failed -> DialogUtils.showOkAlert(this, "EmbelTech", it.error)
        }
    }

    private fun showDialogMessage(message: String) {
        val alertDialog: Dialog?

        val builder = AlertDialog.Builder(this)

        builder.setTitle("EmbelTech").setMessage(message)

        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
            if (isUpdate) {
                val intent = Intent()
                intent.putExtra("result", isUpdate)
                setResult(1000, intent)
            }
            this.finish()
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.setOnDismissListener {
        }
        alertDialog.show()
    }

    private fun clearAllData() {
        imei.setText("")
        imsi.setText("")
        amrid.setText("")
        sim.setText("")

        wakeTime.setText("")
        timezone.setSelection(0)
        dataSample.setText("")
        litrePerPulse.setText("")

        applicationOfAmr.setSelection(0)
        type.setSelection(0)
        diameterSize.setSelection(0)

        state.setText("")
        city.setText("")
        customerDeviceAddress.setText("")
        customerDeviceLocation.setText("")
        customerName.setText("")
        wingOrBuilding.setText("")
        pricePerLiter.setText("")
        area.setText("")
        zone.setText("")
        meterStartReading.setText("")
        serialNo.setText("")
        macId.setText("")
        deviceName.setText("")
        deviceType.setSelection(0)
        user.setSelection(0)
        admin.setSelection(0)
    }

    private fun updateAdmin(admins: List<Admin>) {
        allAdminList = admins
        val adminList: ArrayList<String> = ArrayList()
        for (i in admins) {
            adminList.add("${i.firstname} ${i.lastname}")
        }
        if (adminList.isNotEmpty()) {
            adminList.add(0, "Select Admin")
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, adminList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            admin.adapter = adapter
        }

        /*if (deviceData != null) {
            val selectedAdmin = deviceData!!.deviceAdmin
            if (allAdminList != null && allAdminList.isNotEmpty()) {
                for (i in allAdminList.indices) {
                    val name = "${allAdminList[i].firstname} ${allAdminList[i].lastname}"
                    if (selectedAdmin.equals(name)) {
                        admin.setSelection(i + 1)
                    }
                }
            }
        }*/
    }

    private fun updateView(users: List<User>) {
        allUserList = users
        val userList: ArrayList<String> = ArrayList()
        for (i in users) {
            userList.add("${i.firstname} ${i.lastname}")
        }
        if (userList.isNotEmpty()) {
            userList.add(0, "Select User")
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, userList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            user.adapter = adapter
        }

        /*if (deviceData != null) {
            val selectedUser = deviceData!!.deviceUser

            if (allUserList != null && allUserList.isNotEmpty()) {
                for (i in allUserList.indices) {
                    val name = "${allUserList[i].firstname} ${allUserList[i].lastname}"
                    if (selectedUser.equals(name)) {
                        user.setSelection(i + 1)
                    }
                }
            }
        }*/
    }

    private fun setTimeZonesToSpinner(timezones: Array<String>?) {
        if (timezones != null && timezones.isNotEmpty()) {
            timezones[0] = "Asia/Calcutta"
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, timezones)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            timezone.adapter = adapter
          //  val position=adapter.getPosition("Asia/Calcutta")
           // timezone.setSelection(position)
        }
    }
}
