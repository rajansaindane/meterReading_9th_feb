package com.embeltech.meterreading.ui.report

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.BaseFragment
import com.embeltech.meterreading.livedata.GetAllDeviceListFromDB
import com.embeltech.meterreading.livedata.GetDurationData
import com.embeltech.meterreading.livedata.GetReportData
import com.embeltech.meterreading.livedata.Status
import com.embeltech.meterreading.ui.report.model.ReportResponseItem
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ReportFragment : BaseFragment() {

    var cal = Calendar.getInstance()
    var txtStartDate: TextView? = null
    var txtEndDate: TextView? = null
    var btnGenerate: Button? = null
    var btnStat: Button? = null
    var btnLastMonth: Button? = null
    var btnLastWeek: Button? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var reportViewModel: ReportViewModel


    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var recyclerTableView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentComponent.inject(this)
        reportViewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(ReportViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_report, container, false)
        //Ltr. Consumption
        txtStartDate=root.findViewById(R.id.txtStartDate);
        txtEndDate=root.findViewById(R.id.txtEndDate);
        val simpleDateFormat=SimpleDateFormat("yyy-MM-dd")
        txtEndDate?.setText(simpleDateFormat.format(Date()))
        btnGenerate=root.findViewById(R.id.btnGenerate);
        btnStat=root.findViewById(R.id.button);
        devicesRecyclerView = root.findViewById(R.id.devicesRecyclerView)
        recyclerTableView = root.findViewById(R.id.recyclerTableView)
        btnLastMonth = root.findViewById(R.id.btnLastMonth)
        btnLastWeek = root.findViewById(R.id.btnLastWeek)

        devicesRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerTableView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        reportViewModel.getAllDevices()
        reportViewModel.getStatus().observe(viewLifecycleOwner) {
            handleStatus(it)
        }


        txtStartDate!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(context!!,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateDateInView(txtStartDate!!)
                    },
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        txtEndDate!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(context!!,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateDateInView(txtEndDate!!)
                    },
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        btnGenerate!!.setOnClickListener {
            if ( txtStartDate!!.text.isNullOrEmpty()){
                Toast.makeText(context, "select Start date", Toast.LENGTH_SHORT).show()
            }
            else if (txtEndDate!!.text.isNullOrEmpty()){
                Toast.makeText(context, "select End date", Toast.LENGTH_SHORT).show()
            }
            else {
                reportViewModel.getReportData(
                    txtStartDate!!.text.toString(),
                    txtEndDate!!.text.toString()
                )
            }
            }
        btnStat?.setOnClickListener {
            startActivity(Intent(context,StatActivity::class.java))
        }

        btnLastMonth!!.setOnClickListener {
            reportViewModel.getDurationData("lastMonth")
        }

        btnLastWeek!!.setOnClickListener {
            reportViewModel.getDurationData("lastWeek")
        }

        return root
    }

    private fun updateDateInView(textView:TextView) {
        val myFormat = "yyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textView.text = sdf.format(cal.getTime())
    }

    private fun handleStatus(it: Status?) {
        when(it){
            is GetAllDeviceListFromDB->{

            }
            is GetReportData ->{
                if (it.reportDataList.isNotEmpty()){
                    recyclerTableView.adapter = ReportRecyclerAdapter(requireContext(),
                        it.reportDataList as List<ReportResponseItem>?
                    )
                    recyclerTableView.adapter?.notifyDataSetChanged()
                }
                Log.i("@report","=====>"+ it.reportDataList!![1])
            }

            is GetDurationData ->{
                recyclerTableView.adapter = ReportRecyclerAdapter(requireContext(),
                    it.reportDataList as List<ReportResponseItem>?
                )
                recyclerTableView.adapter?.notifyDataSetChanged()
            }
        }
    }
}