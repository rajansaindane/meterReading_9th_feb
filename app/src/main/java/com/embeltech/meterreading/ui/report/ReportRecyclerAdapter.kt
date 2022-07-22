package com.embeltech.meterreading.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.embeltech.meterreading.R
import com.embeltech.meterreading.ui.report.model.ReportResponseItem
import kotlinx.android.synthetic.main.table_item.view.*

class ReportRecyclerAdapter (
    private val context: Context,
private val reportItems: List<ReportResponseItem>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.table_item, parent, false)
        return ReportRecyclerVH(view)
    }

    override fun getItemCount(): Int {
       return reportItems!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val report = reportItems!![position]
        if (holder is ReportRecyclerVH){
            holder.macId.text=report.macAddress!!.trim()+"\n"
            holder.username.text=report.beaconNname!!.trim()+"\n"
            holder.pulse.text=report.pluseCount!!.trim()+"Ltr"+"\n"
            holder.dateTime.text=report.date+","+report.time
        }
    }

    inner class ReportRecyclerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var macId: TextView = itemView.table_macId
        var username: TextView = itemView.table_username
        var pulse: TextView = itemView.table_pulse
        var dateTime: TextView = itemView.table_date_time
    }

}
