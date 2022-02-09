package com.embeltech.meterreading.ui.billing

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.BaseFragment
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.billing.adapter.BillingDeviceDetailsAdapter
import com.embeltech.meterreading.ui.billing.model.DeviceDataDetail
import com.embeltech.meterreading.utils.DialogUtils
import com.embeltech.meterreading.utils.Utility
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class BillingFragment : BaseFragment() {

    private lateinit var billingViewModel: BillingViewModel
    private var pricePerLiter:Double?=0.0
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var cal = Calendar.getInstance()
    private var isStartDateSelected: Boolean = false

    private lateinit var startDate: AppCompatTextView
    private lateinit var endDate: AppCompatTextView
    private lateinit var amrIdSpinner: AppCompatSpinner
    private lateinit var deviceDetailsRecyclerView: RecyclerView
    private lateinit var getTotal: AppCompatButton
    private lateinit var getInvoice: AppCompatButton
    private lateinit var totalConsumption: AppCompatTextView
    private var deviceDetailsList: ArrayList<DeviceDataDetail> = ArrayList()
    private var selectedAMRId = ""
    private var selectedStartDate = ""
    private var selectedEndDate = ""
    private var amrIdList: ArrayList<String> = ArrayList()
    private lateinit var billingDeviceDetailsAdapter: BillingDeviceDetailsAdapter
    private var totalConsumptionReading: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentComponent.inject(this)
        billingViewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(BillingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_billing, container, false)

        startDate = root.findViewById(R.id.startDate)
        endDate = root.findViewById(R.id.endDate)
        amrIdSpinner = root.findViewById(R.id.amrIdSpinner)
        getTotal = root.findViewById(R.id.getTotal)
        getInvoice = root.findViewById(R.id.getInvoice)
        totalConsumption = root.findViewById(R.id.totalConsumption)
        deviceDetailsRecyclerView = root.findViewById(R.id.deviceDetailsRecyclerView)
        deviceDetailsRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            RecyclerView.VERTICAL, false
        )
        selectedEndDate = Utility.getCurrentDateAndTime()
        selectedStartDate = Utility.getCurrentDateAndTime()
        billingDeviceDetailsAdapter =
            BillingDeviceDetailsAdapter(requireActivity(), deviceDetailsList,pricePerLiter)
        deviceDetailsRecyclerView.adapter = billingDeviceDetailsAdapter
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        billingViewModel.getAllAmrIdList()
        startDate.setOnClickListener {
            DatePickerDialog(
                requireActivity(),
                { _, year, month, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    startDate.text = simpleDateFormat.format(cal.time)
                    selectedStartDate = startDate.text.toString()
                    deviceDetailsList[0].startDate = selectedStartDate
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

        endDate.setOnClickListener {
            isStartDateSelected = false
            DatePickerDialog(
                requireActivity(),
                { _, year, month, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    endDate.text = simpleDateFormat.format(cal.time)
                    selectedEndDate = endDate.text.toString()
                    deviceDetailsList[0].endDate = selectedEndDate
                    billingViewModel.getTotalVolumeAgainstAMRId(
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

        billingViewModel.getStatus().observe(viewLifecycleOwner, {
            handleStatus(it)
        })
        amrIdSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    selectedAMRId = amrIdList[position]
                    billingViewModel.getDeviceDetails(amrIdSpinner.selectedItem.toString())

                    billingViewModel.getLastBillNumber(selectedAMRId)

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        getTotal.setOnClickListener {
            if (billingDeviceDetailsAdapter != null) {
                val pricePerLiter = billingDeviceDetailsAdapter.getPricePerLiter()
                if (pricePerLiter.toString().isNotEmpty() && pricePerLiter > 0) {
                    val device = deviceDetailsList[0]
                    device.pricePerLiter = pricePerLiter
                    totalConsumptionReading = device.totalConsumption * pricePerLiter
                    totalConsumption.text = "Rs. $totalConsumptionReading"
                    device.totalAmount = totalConsumptionReading
                    val userDetails = device.nameAndAddress.split(",")
                    if (userDetails.isNotEmpty()) {
                        device.billUserName = userDetails[0]
                        device.billUserId = userDetails[4].toLong()
                        device.billingAddress = userDetails[2]
                        device.billingCity = userDetails[1]
                        device.billingState = userDetails[3]
                    }
                    billingViewModel.getBillingDetails(deviceDetailsList[0])
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Please enter price per liter",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        getInvoice.setOnClickListener {
            billingViewModel.getBillingInvoice(deviceDetailsList[0].billNumber)
        }

        return root
    }

    private fun updateRecyclerViewData(deviceDetailsList: ArrayList<DeviceDataDetail>) {
        billingDeviceDetailsAdapter.updateData(deviceDetailsList,pricePerLiter)
        billingDeviceDetailsAdapter.notifyDataSetChanged()
    }

    private fun handleStatus(it: Status?) {
        when (it) {
            is ShowProgressDialog -> {
                when (it.message) {
                    Constants.Progress.HIDE_PROGRESS -> hideProgressDialog()
                    Constants.Progress.SHOW_PROGRESS -> showProgressDialog()
                }
            }
            is GetAMRIdList -> {
                if (it.amrIds.isNotEmpty()) {
                    amrIdList = it.amrIds as ArrayList<String>
                    setDataToUI(amrIdList)
                } else {
                    DialogUtils.showOkAlert(requireActivity(), "Meter Reading", "Data not found")
                }
            }
            is Failed -> {
                DialogUtils.showOkAlert(requireActivity(), "Meter Reading", it.error)
            }
            is GetLastBillNumber -> {
                val deviceDataDetail = DeviceDataDetail()
                deviceDataDetail.billNumber = it.billNumber.toLong()
                deviceDataDetail.amrId = selectedAMRId
                deviceDetailsList.clear()
                deviceDetailsList.add(deviceDataDetail)
                updateRecyclerViewData(deviceDetailsList)
            }
            is GetTotalConsumption -> {
                if (it.consumptionUnits[0] != null)
                    for (i in 0 until deviceDetailsList.size) {
                        if (deviceDetailsList[0].amrId == selectedAMRId) {
                            deviceDetailsList[0].totalConsumption =
                                it.consumptionUnits[0].toDouble()
                            updateRecyclerViewData(deviceDetailsList)
                        }
                    }
            }
            is GetDeviceDetails ->{
                pricePerLiter = it.deviceDetails.rate
            }
            is GetUserNameAndAddress -> {
                for (i in 0 until deviceDetailsList.size) {
                    if (deviceDetailsList[0].amrId == selectedAMRId) {
                        deviceDetailsList[0].nameAndAddress = it.userNameAndAddress[0]
                        updateRecyclerViewData(deviceDetailsList)
                    }
                }
            }
            is BillingDataSavedSuccessfully -> {
                getInvoice.visibility = View.VISIBLE
            }
            else -> {
            }


        }

    }

    private fun setDataToUI(amrIds: ArrayList<String>) {
        amrIds.add(0, "Select BLE Id")
        val adapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, amrIds)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        amrIdSpinner.adapter = adapter
    }
}