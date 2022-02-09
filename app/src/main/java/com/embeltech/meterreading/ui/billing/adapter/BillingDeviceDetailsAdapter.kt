package com.embeltech.meterreading.ui.billing.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.embeltech.meterreading.R
import com.embeltech.meterreading.ui.billing.model.DeviceDataDetail
import com.embeltech.meterreading.utils.Utility
import java.util.*

class BillingDeviceDetailsAdapter(
    private val context: Context,
    private var deviceList: ArrayList<DeviceDataDetail>?,
    private var pricePerLiter:Double?

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_billing_device_details, parent, false)
        return DeviceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (deviceList!!.isNotEmpty())
            deviceList!!.size
        else 1
    }

    /**
     * Sets beacon details
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (deviceList!!.isNotEmpty()) {
            val device = deviceList!![position]
            if (holder is DeviceViewHolder && device != null) {
                holder.nameAndAddress.text = device.nameAndAddress
                holder.amrId.text = "AMR ID : ${device.amrId}"
                holder.billNumber.text = "Bill Number : ${device.billNumber}"
                holder.deviceDetailDate.text = Utility.getCurrentDateAndTime()
                device.billingDate = Utility.getCurrentDateAndTime()
                holder.totalConsumption.text = "Total Consumption : ${device.totalConsumption}"
                holder.pricePerLiter.text=pricePerLiter.toString()
                val pricePerLiter = holder.pricePerLiter.text.toString()
                if (pricePerLiter.isNotEmpty()) {
                    device.pricePerLiter = pricePerLiter.toDouble()
                }
//                holder.pricePerLiter.addTextChangedListener(object : TextWatcher {
//                    override fun beforeTextChanged(
//                        s: CharSequence?,
//                        start: Int,
//                        count: Int,
//                        after: Int
//                    ) {
//
//                    }
//
//                    override fun onTextChanged(
//                        s: CharSequence?,
//                        start: Int,
//                        before: Int,
//                        count: Int
//                    ) {
//                    }
//
//                    override fun afterTextChanged(s: Editable?) {
//                        if (s!!.isNotEmpty())
//                            device.pricePerLiter = s.toString().toDouble()
//                    }
//                })
            }
        }
    }

    fun updateData(
        deviceDetailsList: ArrayList<DeviceDataDetail>,
        priceInLiter: Double?
    ) {
        deviceList = deviceDetailsList
        pricePerLiter=priceInLiter
    }

    fun getPricePerLiter(): Double {
        if (deviceList!!.isNotEmpty()) {
            return deviceList!![0].pricePerLiter
        }
        return 0.0
    }

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceDetailDate = itemView.findViewById<AppCompatTextView>(R.id.date)!!
        val nameAndAddress = itemView.findViewById<AppCompatTextView>(R.id.nameAndAddress)!!
        val amrId = itemView.findViewById<AppCompatTextView>(R.id.amrId)!!
        val billNumber: AppCompatTextView = itemView.findViewById(R.id.billNumber)!!
        val totalConsumption = itemView.findViewById<AppCompatTextView>(R.id.totalConsumption)!!
        val pricePerLiter = itemView.findViewById<AppCompatTextView>(R.id.pricePerLiter)!!
    }
}