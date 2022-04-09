package com.embeltech.meterreading.ui.scanbeacon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.embeltech.meterreading.R
import com.embeltech.meterreading.data.database.model.MeterBeacon
import com.embeltech.meterreading.eventbus.RxBus
import kotlinx.android.synthetic.main.row_beacon.view.*

/**
 * Displays nearby beacons scanned by android device
 */
class ScannedBeaconAdapter(
    private val context: Context,
    private val scannedBeacons: List<MeterBeacon>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_beacon, parent, false)
        return ScannedBeaconVH(view)
    }

    /**
     * Returns size of scanned beacons
     */
    override fun getItemCount(): Int {
        return scannedBeacons!!.size
    }

    /**
     * Sets beacon details
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val beacon = scannedBeacons!![position]
        RxBus.publish(RxBus.SUBJECT_SCANNED_BEACON_SELECTED, beacon)
        if (holder is ScannedBeaconVH) {
            holder.txtBeaconName.text = beacon.beaconName
            holder.txtBeaconMacId.text = beacon.beaconMacId

            holder.linearBeacon.setOnClickListener {
            RxBus.publish(RxBus.SUBJECT_SCANNED_BEACON_SELECTED, beacon)

            }
        }
    }

    inner class ScannedBeaconVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtBeaconName: TextView = itemView.txtBeaconName
        var txtBeaconMacId: TextView = itemView.txtBeaconMacId
        var linearBeacon: LinearLayout = itemView.linearBeacon
    }



}