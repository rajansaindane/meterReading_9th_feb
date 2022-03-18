package com.embeltech.meterreading.ui.device

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.BaseFragment
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.device.adapter.DeviceAdapter
import com.embeltech.meterreading.ui.device.model.DeviceListResponse
import com.embeltech.meterreading.ui.device.model.stat_model.StatisticResponsesItem
import com.embeltech.meterreading.ui.device.model.stat_model.TotalConsumptionResponse
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class DeviceDetailsFragment : BaseFragment() {

    private lateinit var deviceDetailsViewModel: DeviceDetailsViewModel
    var pieChart: BarChart? = null
    var txtConsumption: TextView? = null
    var pieChart_user: PieChart? = null
    var buttonWeek: Button? = null
    var buttonMonth: Button? = null
    var buttonCustom: Button? = null
    var fromDate: Date?=null;
    var toDate: Date?=null;
    var cal = Calendar.getInstance()
    private val MAX_X_VALUE = 7
    private val MAX_Y_VALUE = 50
    private val MIN_Y_VALUE = 5
    private val SET_LABEL = "App Downloads"
    private val DAYS =
        arrayOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private var scoreList = ArrayList<StatisticResponsesItem>()
    private lateinit var deviceRecyclerView: RecyclerView
    private lateinit var selectUserLabel: AppCompatTextView
    private lateinit var userSpinner: AppCompatSpinner
    private var selectedUser = ""
    private var deviceList: ArrayList<DeviceListResponse> = ArrayList()
    private var userList: ArrayList<String> = ArrayList()
    private lateinit var deviceAdapter: DeviceAdapter
    private lateinit var selectedDevice: DeviceListResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentComponent.inject(this)
        deviceDetailsViewModel =
            ViewModelProviders.of(this, factory).get(DeviceDetailsViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_device_details, container, false)
        val fabCreateDevice: FloatingActionButton = root.findViewById(R.id.fabCreateDevice)
//        selectUserLabel = root.findViewById(R.id.selectUserTextView)
//        userSpinner = root.findViewById(R.id.userSpinner)

       // deviceRecyclerView = root.findViewById(R.id.devicesRecyclerView)

//        deviceRecyclerView.layoutManager =
//            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        deviceDetailsViewModel.getStatus().observe(requireActivity(), {
            handleStatus(it)
        })

        val role = appPreferences.getUserRole()
        if (role.equals("super-admin")|| role.equals("admin")) {
            fabCreateDevice.visibility = View.VISIBLE

        } else if (role.equals("user")) {
            fabCreateDevice.visibility = View.GONE
        }

//        userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//                if (position > 0) {
//                    selectedUser = userList[position]
//                    updateListData(selectedUser)
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//
//            }
//        }
        fabCreateDevice.setOnClickListener {
            startCreateDeviceActivity(null, false)
        }
        //registerDeviceSelectedObserver()
        pieChart = root.findViewById(R.id.pieChart_view)
        pieChart_user = root.findViewById(R.id.pieChart_view_user)
        buttonWeek=root.findViewById(R.id.buttonWeek);
        buttonMonth=root.findViewById(R.id.buttonMonth);
        buttonCustom=root.findViewById(R.id.buttonCustom);
        txtConsumption=root.findViewById(R.id.txtTotalConsumptionForUser);
        buttonWeek?.setOnClickListener {


                loadStatisticData("lastWeek");

            }
        buttonMonth?.setOnClickListener {

                loadStatisticData("lastMonth");

            }
        buttonCustom?.setOnClickListener {
            showDialog()
        }

            loadStatisticData("lastMonth");


        return root
    }

    private fun loadStatisticData(s: String) {
        if (appPreferences.getUserRole().equals("user")){
            deviceDetailsViewModel.getStatisticsResponseData(
                s,
                "",
                ""
            )
        }
        else {
            deviceDetailsViewModel.getStatisticsResponseData(
                s,
                "",
                ""
            )
        }
    }

//    private fun showPieChart(statisticResponseData: List<StatisticResponsesItem>) {
//        val pieEntries: ArrayList<PieEntry> = ArrayList()
//        val label = "type"
//
//
//        //initializing data
//        val typeAmountMap: MutableMap<String, Float> = HashMap()
//        for (item in statisticResponseData){
//            typeAmountMap[item.deviceOrBeaconName.toString()] = item.pluseCount!! as Float
//        }
////        typeAmountMap["MAC"] = 200
////        typeAmountMap["Snacks"] = 230
////        typeAmountMap["Clothes"] = 100
////        typeAmountMap["Stationary"] = 500
////        typeAmountMap["Phone"] = 50
//
//        //initializing colors for the entries
//        val colors: ArrayList<Int> = ArrayList()
//        colors.add(Color.parseColor("#304567"))
//        colors.add(Color.parseColor("#309967"))
//        colors.add(Color.parseColor("#476567"))
//        colors.add(Color.parseColor("#890567"))
//        colors.add(Color.parseColor("#a35567"))
//        colors.add(Color.parseColor("#ff5f67"))
//        colors.add(Color.parseColor("#3ca567"))
//
//        //input data and fit data into pie chart entry
//        for (type in typeAmountMap.keys) {
//            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type as Float))
//        }
//
//        //collecting the entries with label name
//        val pieDataSet = PieDataSet(pieEntries, label)
//        //setting text size of the value
//        pieDataSet.valueTextSize = 12f
//        //providing color list for coloring different entries
//        pieDataSet.colors = colors
//        //grouping the data set from entry to chart
//        val pieData = PieData(pieDataSet)
//        //showing the value of the entries, default true if not set
//        pieData.setDrawValues(true)
//        pieChart_user!!.data = pieData
//        pieChart_user!!.invalidate()
//    }

    private fun showPieChart(totalConsumption: TotalConsumptionResponse) {
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = "type"

        //initializing data
//        val typeAmountMap: MutableMap<String, Int> = HashMap()
//        typeAmountMap["Toys"] = 200
//        typeAmountMap["Snacks"] = 230
//        typeAmountMap["Clothes"] = 100
//        typeAmountMap["Stationary"] = 500
//        typeAmountMap["Phone"] = 50
       // Log.i("@Device", "map===========>$statisticResponseData")
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["Ltr"] = totalConsumption.lTotalConsumption!!

//        for (item in statisticResponseData){
//            Log.i("@Device", "map1===========>$item")
//            typeAmountMap["Ltr\n"+item.deviceOrBeaconName.toString()] = item.pluseCount!!
//
//        }

        //initializing colors for the entries
        val colors: ArrayList<Int> = ArrayList()
//        colors.add(Color.parseColor("#304567"))
//        colors.add(Color.parseColor("#309967"))
//        colors.add(Color.parseColor("#476567"))
//        colors.add(Color.parseColor("#890567"))
//        colors.add(Color.parseColor("#a35567"))
//        colors.add(Color.parseColor("#ff5f67"))
        colors.add(Color.parseColor("#3ca567"))
        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }

        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        pieDataSet.valueTextSize = 20f
        //providing color list for coloring different entries
        pieDataSet.colors = colors
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true)
        pieChart_user!!.data = pieData
        pieChart_user!!.invalidate()
        //pieChart!!.animateY(500,Easing.EaseInOutCubic)
        pieChart_user!!.spin( 1500,0F,360f, Easing.EaseInCirc);

    }

    override fun onResume() {
        super.onResume()
        deviceDetailsViewModel.getAllDevices()
    }

//    private fun updateListData(selectedUser: String) {
//        val newList = ArrayList<DeviceListResponse>()
//        if (deviceList.isNotEmpty()) {
//            for (i in deviceList.indices) {
//                if (deviceList[i].deviceUser.equals(selectedUser)) {
//                    newList.add(deviceList[i])
//                }
//            }
//        }
//        if (newList.isNotEmpty() && deviceAdapter != null) {
//            deviceAdapter.updateData(newList)
//            deviceAdapter.notifyDataSetChanged()
//        }
//    }

//    private fun registerDeviceSelectedObserver() {
//        RxBus.subscribe(RxBus.SUBJECT_DEVICE_SELECTED, this) {
//            if (it is DeviceListResponse) {
//                selectedDevice = it
//                startCreateDeviceActivity(selectedDevice, true)
//            }
//        }
//    }

    private fun startCreateDeviceActivity(device: DeviceListResponse?, isEdit: Boolean) {
        val intent = Intent(activity, CreateDeviceActivity::class.java)
        intent.putExtra("selected_device", device)
        intent.putExtra("is_edit", isEdit)
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && data != null) {
            val value = data.extras!!.get("result") as Boolean
            if (value) {
                deviceList.remove(selectedDevice)
                deviceAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun handleStatus(it: Status?) {
        when (it) {
            is ShowProgressDialog -> {
                when (it.message) {
                    Constants.Progress.HIDE_PROGRESS -> hideProgressDialog()
                    Constants.Progress.SHOW_PROGRESS -> showProgressDialog()
                }
            }
            is GetStatisticDataResponse -> {
                //showPieChart(it.statisticResponseData)
                if (it.statisticResponseData.isNotEmpty()){
                    scoreList.clear()
                    scoreList.addAll(it.statisticResponseData)
                    if (appPreferences.getUserRole().equals("user")){
                        if (scoreList.size==1){
                            pieChart_user?.visibility=View.VISIBLE
                            pieChart?.visibility=View.INVISIBLE
                            deviceDetailsViewModel.getTotalConsumption()
                        }
                        else {
                            pieChart_user?.visibility=View.INVISIBLE
                            pieChart?.visibility=View.VISIBLE
                            initBarChart(scoreList,1)
                        }
                    }
                    else {
                        pieChart_user?.visibility=View.INVISIBLE
                        pieChart?.visibility=View.VISIBLE
                        initBarChart(scoreList,2)
                    }
                }
                else{
                    Toast.makeText(context, "No Records Found", Toast.LENGTH_SHORT).show()
                }


            }
            is SaveTotalConsumption -> {
//                pieChart_user?.visibility=View.VISIBLE
//                pieChart?.visibility=View.INVISIBLE
                txtConsumption!!.text = "Total Consumption : "+it.totalConsumption.lTotalConsumption.toString()+"Ltr"
                showPieChart(it.totalConsumption)
            }
            is DeviceList -> {
                for(item in it.list){

                    Log.i("@Device","device list =====>"+item.toString())
                }
                setListToAdapter(it.list as ArrayList<DeviceListResponse>)
            }
//            is GetUserList -> updateView(it.users)
//            else -> {
//            }
        }
    }

//    private fun updateView(users: List<User>) {
//        val list: ArrayList<String> = ArrayList()
//        for (i in users) {
//            list.add("${i.firstname} ${i.lastname}")
//        }
//        userList = list
//        if (list.isNotEmpty()) {
//            list.add(0, "Select User")
//            val adapter =
//                ArrayAdapter(
//                    requireActivity(),
//                    android.R.layout.simple_spinner_dropdown_item,
//                    list
//                )
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            userSpinner.adapter = adapter
//        }
//    }

    private fun setListToAdapter(list: ArrayList<DeviceListResponse>) {
        deviceList = list
        val newList = ArrayList<DeviceListResponse>()
        for (i in deviceList.indices) {
            if (deviceList[i].deviceTypeAmrOrBle.equals("ble", true))
                newList.add(deviceList[i])
        }
        deviceAdapter = DeviceAdapter(requireActivity(), newList, appPreferences.getUserRole()!!)
//        deviceRecyclerView.adapter = deviceAdapter
    }

    private fun showBarChart(statisticResponseData: List<StatisticResponsesItem>) {
        val entries: ArrayList<BarEntry> = ArrayList()
        for (i in statisticResponseData.indices) {
            val score = statisticResponseData[i]
            entries.add(BarEntry(i.toFloat(), score.pluseCount!!.toFloat()))
        }

        val barDataSet = BarDataSet(entries, "")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data = BarData(barDataSet)
        pieChart?.data = data
        pieChart?.invalidate()
    }

    private fun initBarChart(statisticResponseData: List<StatisticResponsesItem>,sizeOf:Int) {
        if (sizeOf==1){
            txtConsumption!!.isVisible=true
            deviceDetailsViewModel.getTotalConsumption()
        }
        else{
            txtConsumption!!.isVisible=false
        }
//        hide grid lines
        pieChart?.axisLeft!!.setDrawGridLines(false)
        val xAxis: XAxis = pieChart!!.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        pieChart!!.axisRight.isEnabled = false
        pieChart!!.description.text="Consumption In Ltr                                                      "
       // pieChart!!.description.setPosition(3F,3F)
        //remove legend
        pieChart!!.legend.isEnabled = false


        //remove description label
       // pieChart!!.description.isEnabled = false


        //add animation
        pieChart!!.animateY(3000)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter(statisticResponseData)
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f
        showBarChart(statisticResponseData)
    }

    inner class MyAxisFormatter(statisticResponseData: List<StatisticResponsesItem>) : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            Log.d("@Stat", "getAxisLabel: index $index")
            return if (index < scoreList.size) {
                  if(scoreList[index].batteryPercentage !=null)  scoreList[index].batteryPercentage.toString() + "%\t[" + scoreList[index].deviceOrBeaconName.toString() + "]"
                  else
                      "0%\t[" + scoreList[index].deviceOrBeaconName.toString() + "]"
                } else {
                ""
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_stat)
        val btnGenerate = dialog.findViewById(R.id.btnGenerateStat) as Button
        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val txtStartDateStat = dialog.findViewById(R.id.txtStartDateStat) as TextView
        val txtEndDateStat = dialog.findViewById(R.id.txtEndDateStat) as TextView
        txtStartDateStat.setOnClickListener {
            DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView(txtStartDateStat!!)
                },
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        txtEndDateStat.setOnClickListener {
            DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView(txtEndDateStat!!)
                },
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnGenerate.setOnClickListener {
            if (!txtStartDateStat.text.toString().isNullOrEmpty()
                && !txtEndDateStat.text.toString().isNullOrEmpty()){
                deviceDetailsViewModel.getStatisticsResponseData(
                    "",
                    txtStartDateStat.text.toString(),
                    txtEndDateStat.text.toString()
                )
                dialog.dismiss()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateDateInView(textView:TextView) {
        val myFormat = "yyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textView.text = sdf.format(cal.getTime())
    }

}