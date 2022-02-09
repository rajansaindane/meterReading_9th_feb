package com.embeltech.meterreading.ui.device.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.embeltech.meterreading.R
import com.embeltech.meterreading.eventbus.RxBus
import com.embeltech.meterreading.ui.device.model.DeviceListResponse
import java.util.*

class DeviceAdapter(
    private val context: Context,
    private val deviceList: ArrayList<DeviceListResponse>?,
    private val userRole: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_device, parent, false)
        return DeviceViewHolder(view)
    }

    /**
     * Returns size of scanned beacons
     */
    override fun getItemCount(): Int {
        return deviceList!!.size
    }

    /**
     * Sets beacon details
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val device = deviceList!![position]

        if (holder is DeviceViewHolder) {
            holder.deviceAmrId.text = device.deviceAmrId
//            holder.wakeUpTime.text = device.deviceWakeupTime

            if (userRole.equals("super-admin")) {
                holder.deviceImeiNumber.text = device.deviceImei
                holder.deviceImsiNumber.text = device.deviceImsi
                holder.deviceCustomerName.text = device.deviceCustomerName
                holder.deviceImeiNumberLbl.text = "IMEI Number"
                holder.deviceImsiNumberLbl.text = "IMSI Number"
                holder.deviceCustomerName.visibility = View.VISIBLE
                holder.deviceCustomerNameLbl.visibility = View.VISIBLE
                holder.timeZone.text = device.deviceTimeZone

            } else if (userRole.equals("user") || userRole.equals("admin")) {
                holder.deviceImeiNumber.text = device.deviceWakeupTime
                holder.deviceImsiNumber.text = device.deviceApplicationOfAmr
                holder.deviceImeiNumberLbl.text = "WakeUp Time"
                holder.deviceImsiNumberLbl.text = "Application Of AMR"
                holder.timeZone.text = device.deviceTimeZone
                holder.deviceCustomerName.visibility = View.GONE
                holder.deviceCustomerNameLbl.visibility = View.GONE
            }

            holder.cardDevice.setOnClickListener {
                RxBus.publish(RxBus.SUBJECT_DEVICE_SELECTED, device)
            }
        }
    }

    fun updateData(newList: ArrayList<DeviceListResponse>) {
        deviceList!!.clear()
        deviceList.addAll(newList)
//        notifyDataSetChanged()
    }

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceCustomerName = itemView.findViewById<AppCompatTextView>(R.id.deviceCustomerName)!!
        val deviceImeiNumber = itemView.findViewById<AppCompatTextView>(R.id.deviceImeiNumber)!!
        val deviceImsiNumber = itemView.findViewById<AppCompatTextView>(R.id.deviceImsiNumber)!!
        val cardDevice: CardView = itemView.findViewById(R.id.cardDevice)!!
        val deviceAmrId = itemView.findViewById<AppCompatTextView>(R.id.deviceAmrId)!!

        val deviceCustomerNameLbl = itemView.findViewById<AppCompatTextView>(R.id.customerNameLbl)!!
        val deviceImeiNumberLbl = itemView.findViewById<AppCompatTextView>(R.id.imeiNumberLbl)!!
        val deviceImsiNumberLbl = itemView.findViewById<AppCompatTextView>(R.id.imsiIdLbl)!!
//        val wakeUpTime = itemView.findViewById<AppCompatTextView>(R.id.wakeTime)!!
        val timeZone = itemView.findViewById<AppCompatTextView>(R.id.timezone)!!
    }
}