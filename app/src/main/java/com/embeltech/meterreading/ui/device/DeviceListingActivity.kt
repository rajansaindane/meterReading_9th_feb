package com.embeltech.meterreading.ui.device

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.BaseActivity
import com.embeltech.meterreading.livedata.DeviceList
import com.embeltech.meterreading.livedata.Status
import com.embeltech.meterreading.ui.device.adapter.DeviceAdapter
import com.embeltech.meterreading.ui.device.model.DeviceListResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_create_device.*
import javax.inject.Inject

class DeviceListingActivity : BaseActivity() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var deviceDetailsViewModel: DeviceDetailsViewModel

    private lateinit var deviceRecyclerView: RecyclerView
    private lateinit var deviceAdapter: DeviceAdapter
    private var deviceList: ArrayList<DeviceListResponse> = ArrayList()
    private var isUpdate: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_listing)
        activityComponent.inject(this)
        back.setOnClickListener {
            onBackPressed()
            intent.putExtra("result", isUpdate)
        }
        deviceDetailsViewModel =
            ViewModelProviders.of(this, factory).get(DeviceDetailsViewModel::class.java)

        deviceDetailsViewModel.getStatus().observe(this, {
            handleStatus(it)
        })
        val fabCreateDevice: FloatingActionButton = findViewById(R.id.fabCreateDeviceList)

        deviceRecyclerView = findViewById(R.id.devicesRecyclerView)
        deviceRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        deviceDetailsViewModel.getAllDevices()
        fabCreateDevice.setOnClickListener {
            startCreateDeviceActivity(null, false)
        }
    }

    private fun startCreateDeviceActivity(device: DeviceListResponse?, isEdit: Boolean) {
        val intent = Intent(this, CreateDeviceActivity::class.java)
        intent.putExtra("selected_device", device)
        intent.putExtra("is_edit", isEdit)
        startActivityForResult(intent, 1000)
    }

    private fun setListToAdapter(list: ArrayList<DeviceListResponse>) {
        deviceList = list
        val newList = ArrayList<DeviceListResponse>()
        for (i in deviceList.indices) {
            if (deviceList[i].deviceTypeAmrOrBle.equals("ble", true))
                newList.add(deviceList[i])
        }
        deviceAdapter = DeviceAdapter(this, newList, appPreferences.getUserRole()!!)
        deviceRecyclerView.adapter = deviceAdapter
    }

    private fun handleStatus(it: Status?) {
        when (it) {
            is DeviceList->{
            if (it.list.isNotEmpty()){
                Log.i("@Device","list==========>"+it.list.toString())
                setListToAdapter(it.list as ArrayList<DeviceListResponse>)

            }

            }
        }


        }
}