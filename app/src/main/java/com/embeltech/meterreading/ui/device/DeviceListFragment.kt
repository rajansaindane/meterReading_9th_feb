package com.embeltech.meterreading.ui.device

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.BaseFragment
import com.embeltech.meterreading.livedata.DeviceList
import com.embeltech.meterreading.livedata.Status
import com.embeltech.meterreading.ui.device.adapter.DeviceAdapter
import com.embeltech.meterreading.ui.device.model.DeviceListResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject


class DeviceListFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var deviceDetailsViewModel: DeviceDetailsViewModel

    private lateinit var deviceRecyclerView: RecyclerView
    private lateinit var deviceAdapter: DeviceAdapter
    private var deviceList: ArrayList<DeviceListResponse> = ArrayList()
    private var isUpdate: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentComponent.inject(this)
        deviceDetailsViewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(DeviceDetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_device_list, container, false)
        deviceDetailsViewModel.getStatus().observe(requireActivity(), {
            handleStatus(it)
        })
        val fabCreateDevice: FloatingActionButton = root.findViewById(R.id.fabCreateDeviceList)

        deviceRecyclerView = root.findViewById(R.id.devicesRecyclerView)
        deviceRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        deviceDetailsViewModel.getAllDevices()
        fabCreateDevice.setOnClickListener {
            startCreateDeviceActivity(null, false)
        }
        return root;
    }
    private fun startCreateDeviceActivity(device: DeviceListResponse?, isEdit: Boolean) {
        val intent = Intent(requireContext(), CreateDeviceActivity::class.java)
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
        deviceAdapter = DeviceAdapter(requireContext(), newList, appPreferences.getUserRole()!!)
        deviceRecyclerView.adapter = deviceAdapter
    }

    private fun handleStatus(it: Status?) {
        when (it) {
            is DeviceList ->{
                if (it.list.isNotEmpty()){
                    Log.i("@Device","list==========>"+it.list.toString())
                    setListToAdapter(it.list as ArrayList<DeviceListResponse>)

                }

            }
        }


    }
}

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment DeviceListFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            DeviceListFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
