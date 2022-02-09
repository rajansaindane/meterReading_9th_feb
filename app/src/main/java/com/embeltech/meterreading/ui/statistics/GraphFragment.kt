package com.embeltech.meterreading.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.BaseFragment
import com.embeltech.meterreading.livedata.GetStatisticData
import com.embeltech.meterreading.livedata.Status
import com.embeltech.meterreading.ui.report.ReportViewModel
import com.embeltech.meterreading.ui.statistics.model.StatisticResponseItem
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import javax.inject.Inject


class GraphFragment : BaseFragment() {
    companion object {
        fun newInstance() = GraphFragment()
    }
    private lateinit var graphViewModel: ReportViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var pieChart: PieChart? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentComponent.inject(this)
        graphViewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(ReportViewModel::class.java)
        val root = inflater.inflate(R.layout.graph_fragment, container, false)

        pieChart = root.findViewById(R.id.pieChart_view)
       // showPieChart()

        graphViewModel.getStatisticData()
        graphViewModel.getStatus().observe(viewLifecycleOwner) {
            handleStatus(it)
        }
        setHasOptionsMenu(true)
        return root
    }

    private fun showPieChart(statisticData: List<StatisticResponseItem>) {
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = statisticData[0].macAddress+" / "+statisticData[0].date

        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["pluseCount"] = statisticData[0].pluseCount!!.toInt()
        typeAmountMap["meterReading"] = statisticData[0].meterReading!!.toInt()


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
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
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
        pieChart!!.data = pieData
        pieChart!!.invalidate()
    }


    private fun handleStatus(it: Status?) {
        when(it){

            is GetStatisticData ->{
                if (it.statisticData.isNotEmpty()){
                    showPieChart(it.statisticData!!)
                    Log.i("@report","=====>"+ it.statisticData!![1])
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem? = menu.findItem(R.id.action_scan_device)
        item?.isVisible = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       // graphViewModel = ViewModelProvider(this).get(GraphViewModel::class.java)

    }

}