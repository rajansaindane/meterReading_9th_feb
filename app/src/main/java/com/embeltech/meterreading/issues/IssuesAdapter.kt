package com.embeltech.meterreading.issues

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.embeltech.meterreading.R
import java.util.ArrayList

class IssuesAdapter(
    private val context: Context,
    private val issueList: ArrayList<IssueGetResponseItem?>?,
    private val userRole: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_issue_card, parent, false)
        return IssueViewHolder(view)
    }

    /**
     * Returns size of scanned beacons
     */
    override fun getItemCount(): Int {
        return issueList!!.size
    }

    /**
     * Sets beacon details
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i("@Adapter", "adapter==========>"+ issueList!![position])

        val issue = issueList!![position]

        if (holder is IssueViewHolder) {
            holder.issueCustomerName.text = issue!!.userName
            holder.issueNumber.text = issue!!.contact.toString()
            holder.issue.text = issue.issue


        }
    }

//    fun updateData(newList: ArrayList<DeviceListResponse>) {
//        deviceList!!.clear()
//        deviceList.addAll(newList)
////        notifyDataSetChanged()
//    }

    inner class IssueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val issueCustomerName = itemView.findViewById<AppCompatTextView>(R.id.issueUserName)!!
        val issueNumber = itemView.findViewById<AppCompatTextView>(R.id.issueCustomerNumber)!!
        val issue = itemView.findViewById<AppCompatTextView>(R.id.issue)!!

    }
}