package com.embeltech.meterreading.ui.device

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
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
import com.embeltech.meterreading.ui.device.model.newScreens.ResponseAdminListItem
import com.embeltech.meterreading.ui.device.model.newScreens.ResponseDeviceListItem
import com.embeltech.meterreading.ui.device.model.newScreens.ResponseNewStatisticsItem
import com.embeltech.meterreading.ui.device.model.newScreens.ResponseUserListItem
import com.embeltech.meterreading.ui.device.model.stat_model.StatisticResponsesItem
import com.embeltech.meterreading.ui.device.model.stat_model.TotalConsumptionResponse
import com.embeltech.meterreading.ui.statistics.model.StatisticResponseItem
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_device_details.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class DeviceDetailsFragment : BaseFragment() {

    private lateinit var deviceDetailsViewModel: DeviceDetailsViewModel
    var pieChart: BarChart? = null
    var txtConsumption: TextView? = null
    var txtAdminLabel: TextView? = null
    var txtUserLabel: TextView? = null
    var pieChart_user: PieChart? = null
    var buttonWeek: Button? = null
    var buttonMonth: Button? = null
    var buttonCustom: Button? = null

    var btnAdminCount: Button? = null
    var btnUserCount: Button? = null
    var btnFeedback: Button? = null
    var btnTotalMeter: Button? = null
    var btnNonWorkingMeter: Button? = null
    var btnWorkingMeter: Button? = null
    var btnTotalizer: Button? = null
    var btnTotalizerLastWeek: Button? = null
    var btnContactUs: Button? = null

    var spinnerAdminList: Spinner? = null
    var spinnerUserList: Spinner? = null
    var spinnerDeviceList: Spinner? = null
    var spinnerDurationList: Spinner? = null

    var adminSpinnerList = ArrayList<String>()
    var userSpinnerList = ArrayList<String>()
    var deviceSpinnerList = ArrayList<String>()
    var durationSpinnerList = ArrayList<String>()

    var fromDate: Date? = null;
    var toDate: Date? = null;
    var cal = Calendar.getInstance()
    private val MAX_X_VALUE = 7
    private val MAX_Y_VALUE = 50
    private val MIN_Y_VALUE = 5
    private val SET_LABEL = "App Downloads"
    private val DAYS =
        arrayOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    // private var scoreList = ArrayList<StatisticResponsesItem>()
    private var scoreList = ArrayList<ResponseNewStatisticsItem>()
    private lateinit var deviceRecyclerView: RecyclerView
    private lateinit var selectUserLabel: AppCompatTextView
    private lateinit var userSpinner: AppCompatSpinner
    private var selectedUser = ""
    private var deviceList: ArrayList<DeviceListResponse> = ArrayList()
    private var userList: ArrayList<String> = ArrayList()
    private lateinit var deviceAdapter: DeviceAdapter
    private lateinit var selectedDevice: DeviceListResponse
    private var userId: Long = 0L
    private lateinit var role: String
    private var deviceItemsList: ArrayList<ResponseDeviceListItem> = ArrayList()
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
        btnAdminCount = root.findViewById(R.id.btnAdminUser)
        btnUserCount = root.findViewById(R.id.btnUserCount)
        btnFeedback = root.findViewById(R.id.btnFeedback)
        btnTotalMeter = root.findViewById(R.id.btnTotalMeter)
        btnNonWorkingMeter = root.findViewById(R.id.btnNonWorking)
        btnWorkingMeter = root.findViewById(R.id.btnWorking)
        btnTotalizer = root.findViewById(R.id.btnTotaliser)
        btnTotalizerLastWeek = root.findViewById(R.id.btnTotaliser_last_week)
        btnContactUs = root.findViewById(R.id.btnContactUs)

        spinnerAdminList = root.findViewById(R.id.spinnerAdminList)
        spinnerUserList = root.findViewById(R.id.spinnerUserList)
        spinnerDeviceList = root.findViewById(R.id.spinnerDeviceList)
        spinnerDurationList = root.findViewById(R.id.spinnerDurationList)

        txtAdminLabel = root.findViewById(R.id.txtAdminLabel)
        txtUserLabel = root.findViewById(R.id.txtUserLabel)
//        selectUserLabel = root.findViewById(R.id.selectUserTextView)
//        userSpinner = root.findViewById(R.id.userSpinner)

        // deviceRecyclerView = root.findViewById(R.id.devicesRecyclerView)

//        deviceRecyclerView.layoutManager =
//            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        Log.i("@device", "D role==============>" + appPreferences.getUserRole())
        deviceDetailsViewModel.getStatus().observe(requireActivity(), {
            handleStatus(it)
        })

        role = appPreferences.getUserRole()!!

        userId = appPreferences.getUserId()
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
//        pieChart_user = root.findViewById(R.id.pieChart_view_user)
//        buttonWeek=root.findViewById(R.id.buttonWeek);
//        buttonMonth=root.findViewById(R.id.buttonMonth);
//        buttonCustom=root.findViewById(R.id.buttonCustom);
//        txtConsumption=root.findViewById(R.id.txtTotalConsumptionForUser);
//        buttonWeek?.setOnClickListener {
//
//
//                loadStatisticData("lastWeek");
//
//            }
//        buttonMonth?.setOnClickListener {
//
//                loadStatisticData("lastMonth");
//
//            }
//        buttonCustom?.setOnClickListener {
//            showDialog()
//        }

        durationSpinnerList.add("Duration")
        durationSpinnerList.add("Last Week")
        durationSpinnerList.add("Last Month")
        durationSpinnerList.add("Custom")
        loadStatisticData("lastMonth", userId, role);
        deviceDetailsViewModel.getDashboardAdminList()
        deviceDetailsViewModel.getDashboardUserList(0, "")
        deviceDetailsViewModel.getAlertCount()
        deviceDetailsViewModel.getDashboardDeviceList(0, "")

        spinnerDurationList!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, arg3: Long
            ) {
                Log.i(
                    "@Device",
                    "selected position=====>" + parent.getItemAtPosition(position).toString()
                )

                if (parent.getItemAtPosition(position).toString() == "Last Week") {
                    loadStatisticData("lastWeek", userId, role)
                }
                if (parent.getItemAtPosition(position).toString() == "Last Month") {
                    loadStatisticData("lastMonth", userId, role)
                }
                if (parent.getItemAtPosition(position).toString() == "Custom") {
                    showDialog("custom")
                }

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }

        spinnerDeviceList!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, arg3: Long
            ) {
                Log.i(
                    "@Device",
                    "selected position=====>" + parent.getItemAtPosition(position).toString()
                )

                deviceDetailsViewModel.getDeviceConsumption(deviceItemsList[position].deviceName!!)


            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }

//        if (appPreferences.getUserRole().equals("admin")){
//            spinnerAdminList!!.visibility=View.GONE
//            spinnerUserList!!.gravity=Gravity.CENTER
//        }
        deviceDetailsViewModel.getTotalizerData(
            "lastMonth",
            "",
            "",
            userId,
            role
        )
        btnTotalizer!!.setOnClickListener {
            showDialog("totalizer")
        }

        if (appPreferences.getUserRole().equals("user")) {
            btnAdminCount!!.visibility = View.GONE
            btnUserCount!!.visibility = View.GONE
            btnFeedback!!.visibility = View.GONE
            spinnerUserList!!.visibility = View.INVISIBLE
            spinnerAdminList!!.visibility = View.INVISIBLE
            txtAdminLabel!!.visibility = View.INVISIBLE
            txtUserLabel!!.visibility = View.INVISIBLE
        }
        if (appPreferences.getUserRole().equals("admin")) {
            btnAdminCount!!.text = appPreferences.getUserName()
            spinnerAdminList!!.visibility = View.GONE
            txtAdminLabel!!.visibility = View.INVISIBLE
        }
//        if (role.equals("super-admin") || role.equals("admin")) {
//            pieChart!!.visibility = View.VISIBLE
//            pieChart_user!!.visibility = View.GONE
//        } else if (role.equals("user")) {
//            pieChart!!.visibility = View.GONE
//            pieChart_user!!.visibility = View.VISIBLE
//        }

        return root
    }

    private fun loadStatisticData(s: String, userId: Long, role: String) {
        Log.i("@Device", "selected position=====>" + s)

//        deviceDetailsViewModel.getStatisticsResponseData(
//                s,
//            userId,
//            role,
//                "",
//                ""
//
//            )

        deviceDetailsViewModel.getStatisticsResponseDataTest(
            s,
            userId,
            role,
            "",
            ""

        )
//        if (appPreferences.getUserRole().equals("user")){
//            deviceDetailsViewModel.getStatisticsResponseData(
//                s,
//                "",
//                ""
//            )
//        }
//        else {
//            deviceDetailsViewModel.getStatisticsResponseData(
//                s,
//                "",
//                ""
//            )
//        }
    }

    //private fun showPieChart(statisticResponseData: List<StatisticResponsesItem>) {
//    private fun showPieChart(statisticResponseData: List<ResponseNewStatisticsItem>) {
//        val pieEntries: ArrayList<PieEntry> = ArrayList()
//        val label = "type"
//
//
//        //initializing data
//        val typeAmountMap: MutableMap<String, Float> = HashMap()
//        for (item in statisticResponseData) {
//            typeAmountMap[item.deviceOrBeaconName.toString()] = item.totalconsumption!! as Float
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

    private fun showPieChart(statisticResponseData: List<ResponseNewStatisticsItem>) {
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = statisticResponseData[0].macAddress+" / "+statisticResponseData[0].totalconsumption

        //initializing data
        val typeAmountMap: MutableMap<Any, Float> = HashMap()
        typeAmountMap["pluseCount"] = statisticResponseData[0].totalconsumption!!.toString().toFloat()


        //initializing colors for the entries
        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#304567"))
        colors.add(Color.parseColor("#309967"))
        colors.add(Color.parseColor("#476567"))
        colors.add(Color.parseColor("#890567"))
        colors.add(Color.parseColor("#a35567"))
        colors.add(Color.parseColor("#ff5f67"))
        colors.add(Color.parseColor("#3ca567"))

        //input data and fit data into pie chart entry
        for (type in statisticResponseData) {
            pieEntries.add(PieEntry(type.totalconsumption.toString().toFloat(), type))
        }

        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        //providing color list for coloring different entries
        pieDataSet.colors = colors
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true)
        pieChart_user!!.data = pieData
        pieChart_user!!.invalidate()
    }


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
        pieChart_user!!.spin(1500, 0F, 360f, Easing.EaseInCirc);

    }

    private fun initBarChart(statisticResponseData: List<ResponseNewStatisticsItem>, sizeOf: Int) {
//        if (sizeOf==1){
//            txtConsumption!!.isVisible=true
//            deviceDetailsViewModel.getTotalConsumption()
//        }
//        else{
//            txtConsumption!!.isVisible=false
//        }
//        hide grid lines
        pieChart?.axisLeft!!.setDrawGridLines(false)
        val xAxis: XAxis = pieChart!!.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        pieChart!!.axisRight.isEnabled = false
        pieChart!!.description.text =
            "Consumption In Ltr                                                      "
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
        xAxis.textSize=7F
        showBarChart(statisticResponseData)

    }

    inner class MyAxisFormatter(statisticResponseData: List<ResponseNewStatisticsItem>) :
        IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            Log.d("@Stat", "getAxisLabel: index $index")
            return if (index < scoreList.size) {
                if (scoreList[index].batteryPercentage != null) scoreList[index].batteryPercentage.toString() + "%\t[" + scoreList[index].deviceOrBeaconName.toString() + "]"
                else
                    "0%\t[" + scoreList[index].deviceOrBeaconName.toString() + "]"
            } else {
                ""
            }
        }
    }

    private fun showBarChart(statisticResponseData: List<ResponseNewStatisticsItem>) {
        val entries: ArrayList<BarEntry> = ArrayList()
        for (i in statisticResponseData.indices) {
            val score = statisticResponseData[i]
            Log.i("@Device", "score =====>$score")
            if (score.totalconsumption != null) {
                entries.add(BarEntry(i.toFloat(), score.totalconsumption.toString().toFloat()))
            } else {
                entries.add(BarEntry(i.toFloat(), 0F))
            }
        }

        val barDataSet = BarDataSet(entries, "Ltr")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        barDataSet.valueTextSize=8F

        val data = BarData(barDataSet)
        pieChart?.data = data
        pieChart?.invalidate()
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
        val intent = Intent(activity, DeviceListingActivity::class.java)
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
                    //Constants.Progress.SHOW_PROGRESS -> showProgressDialog()
                }
            }
            is GetStatisticDataResponse -> {
//                if(appPreferences.getUserRole().equals("user")){
//                    txtConsumption!!.visibility=View.VISIBLE
//                    txtConsumption!!.text = "Total Consumption : "+"0 Ltr"
//                }
                //showPieChart(it.statisticResponseData)
                if (it.statisticResponseData.isNotEmpty()) {

                    //  scoreList.clear()
                    // scoreList.addAll(it.statisticResponseData)
                    // pieChart_user?.visibility=View.INVISIBLE
                    //     pieChart?.visibility=View.VISIBLE
                    //    initBarChart(scoreList,2)
//                    deviceDetailsViewModel.getTotalConsumption()
//                    if (appPreferences.getUserRole().equals("user")){
//                        if (scoreList.size==1){
//                            pieChart_user?.visibility=View.VISIBLE
//                            pieChart?.visibility=View.INVISIBLE
//                            deviceDetailsViewModel.getTotalConsumption()
//                        }
//                        else {
//                            pieChart_user?.visibility=View.INVISIBLE
//                            pieChart?.visibility=View.VISIBLE
//                            initBarChart(scoreList,1)
//                        }
//                    }
//                    else {
//                        pieChart_user?.visibility=View.INVISIBLE
//                        pieChart?.visibility=View.VISIBLE
//                        initBarChart(scoreList,2)
//                    }
                } else {
                    Toast.makeText(context, "No Records Found", Toast.LENGTH_SHORT).show()
                }


            }
            is SaveTotalConsumption -> {
//                pieChart_user?.visibility=View.VISIBLE
//                pieChart?.visibility=View.INVISIBLE
                txtConsumption!!.text =
                    "Total Consumption : " + it.totalConsumption.lTotalConsumption.toString() + "Ltr"
                //showPieChart(it.totalConsumption)
            }
            is DeviceList -> {
                for (item in it.list) {

                    Log.i("@Device", "device list =====>" + item.toString())
                }
                setListToAdapter(it.list as ArrayList<DeviceListResponse>)
            }
            is GetDashboardAdminList -> {
                adminSpinnerList.clear()
                setAdminList(it.adminList)

            }
            is GetDashboardUserList -> {
                userSpinnerList.clear()
                setUserList(it.userList)
                Log.i("@Device", "user list =====>" + it.userList)

            }
            is GetAlertCount -> {
                if (it.alterCount.adminUser != null && it.alterCount.adminUser != "0") {
                    if (!appPreferences.getUserRole().equals("admin")) {
                        btnAdminCount?.text = "Admin\n" + it.alterCount.adminUser
                    }
                }
                if (it.alterCount.userCount != null && it.alterCount.userCount != "0") {
                    btnUserCount?.text = "User\n" + it.alterCount.userCount
                }
                if (it.alterCount.feedbackCount != null && it.alterCount.feedbackCount != 0) {
                    btnFeedback?.text = "Feedback\n" + it.alterCount.feedbackCount.toString()
                }
                if (it.alterCount.totalMeters != null && it.alterCount.totalMeters != 0) {
                    btnTotalMeter?.text = "TotalMeter\n" + it.alterCount.totalMeters.toString()
                }
                if (it.alterCount.nonWorkingMeters != null && it.alterCount.nonWorkingMeters != 0) {
                    btnNonWorkingMeter?.text =
                        "NonWorking\n" + it.alterCount.nonWorkingMeters.toString()
                }
                if (it.alterCount.workingMeters != null && it.alterCount.workingMeters != 0) {
                    btnWorkingMeter?.text = "Working\n" + it.alterCount.workingMeters.toString()
                }
                if (it.alterCount.totalizer != null && it.alterCount.totalizer != 0) {
                    btnTotalizer?.text = "Totalizer\n" + it.alterCount.totalizer.toString()
                }
//                if (it.alterCount.wee!=null && it.alterCount.nonWorkingMeters!=0 ){
//                    btnNonWorkingMeter?.text=it.alterCount.nonWorkingMeters.toString()
//                }
                if (it.alterCount.contactusCount != null && it.alterCount.contactusCount != "0") {
                    btnNonWorkingMeter?.text =
                        "ContactUs\n" + it.alterCount.nonWorkingMeters.toString()
                }
                Log.i("@Device", "AlertCount =====>" + it.toString())

            }
            is GetDashboardDeviceList -> {
                deviceSpinnerList.clear()
                setDeviceList(it.deviceList)
            }

            is GetTotalizerData -> {
                btnTotalizer?.text = "Totalizer\n" + it.data.totalConsumption.toString()

            }

            is GetDeviceConsumption -> {
                txtTotalizerOfDevice.text = "Totalizer Value : " + it.totalizer
            }

            is GetStatisticDataResponseTest -> {
                if (it.statisticResponseData.isNotEmpty()) {
                    Log.i("@Device", "new response =====>" + it.statisticResponseData.toString())

                    scoreList.clear()
                    scoreList.addAll(it.statisticResponseData)
                    // pieChart_user?.visibility=View.INVISIBLE
                    //pieChart?.visibility=View.VISIBLE
                    if (appPreferences.getUserRole().equals("user", true)) {
                        initBarChart(scoreList, 2)

                       // showPieChart(scoreList)
                    } else {
                        initBarChart(scoreList, 2)
                    }
                } else {
                    Toast.makeText(context, "No Records Found", Toast.LENGTH_SHORT).show()
                }
            }
//            is GetUserList -> updateView(it.users)
//            else -> {
//            }
        }
    }

    private fun setDeviceList(deviceList: List<ResponseDeviceListItem>) {
        deviceItemsList.addAll(deviceList)
        // deviceSpinnerList.add("Devices")
        for (i in deviceList) {
            deviceSpinnerList.add(i.deviceName!!)
        }

        val deviceListAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            deviceSpinnerList
        )
        spinnerDeviceList!!.adapter = deviceListAdapter

        val durationListAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            durationSpinnerList
        )
        spinnerDurationList!!.adapter = durationListAdapter


    }


    private fun setUserList(userList: List<ResponseUserListItem>) {
        // userSpinnerList.add("Users")
        for (i in userList) {
            userSpinnerList.add(i!!.firstname + " " + i.lastname)
        }

        val userListAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            userSpinnerList
        )
        spinnerUserList!!.adapter = userListAdapter

        spinnerUserList!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, arg3: Long
            ) {
                if (position > 0) {
                    val id: Long = userList[position - 1].pkUserDetails!!.toLong()
                    role = userList[position - 1].role.toString()
                    userId = id
                    deviceDetailsViewModel.getDashboardDeviceList(id, role)
                    loadStatisticData("", id, role)
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }

    }

    private fun setAdminList(adminList: List<ResponseAdminListItem>) {
        //adminSpinnerList.add("Admins")
        for (i in adminList) {
            Log.i("@Device", "admin list =====>" + i!!.firstname)

            adminSpinnerList.add(i!!.firstname + " " + i.lastname)

        }
        val adminListAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            adminSpinnerList
        )
        spinnerAdminList!!.adapter = adminListAdapter
        spinnerAdminList!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, arg3: Long
            ) {
                if (position > 0) {
                    val id: Long = adminList[position - 1].fkAdminId!!.toLong()
                    userId = id
                    role = "admin"
                    deviceDetailsViewModel.getDashboardUserList(id, "admin")
                    loadStatisticData("", id, role)
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
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

    private fun showDialog(callFrom: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_stat)
        val btnGenerate = dialog.findViewById(R.id.btnGenerateStat) as Button
        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val txtStartDateStat = dialog.findViewById(R.id.txtStartDateStat) as TextView
        val txtEndDateStat = dialog.findViewById(R.id.txtEndDateStat) as TextView
        txtStartDateStat.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView(txtStartDateStat!!)
                },
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        txtEndDateStat.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView(txtEndDateStat!!)
                },
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnGenerate.setOnClickListener {
            if (!txtStartDateStat.text.toString().isNullOrEmpty()
                && !txtEndDateStat.text.toString().isNullOrEmpty()
            ) {
                if (callFrom.equals("totalizer")) {
                    deviceDetailsViewModel.getTotalizerData(
                        "",
                        txtStartDateStat.text.toString(),
                        txtEndDateStat.text.toString(),
                        userId, role
                    )
                } else {
                    deviceDetailsViewModel.getStatisticsResponseData(
                        "",
                        userId, role,
                        txtStartDateStat.text.toString(),
                        txtEndDateStat.text.toString()

                    )
                }
                dialog.dismiss()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateDateInView(textView: TextView) {
        val myFormat = "yyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textView.text = sdf.format(cal.getTime())
    }

}