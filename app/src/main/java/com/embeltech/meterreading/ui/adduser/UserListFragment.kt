package com.embeltech.meterreading.ui.adduser

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.embeltech.meterreading.livedata.GetDashboardUserList
import com.embeltech.meterreading.livedata.GetUserList
import com.embeltech.meterreading.livedata.Status
import com.embeltech.meterreading.ui.device.DeviceDetailsViewModel
import com.embeltech.meterreading.ui.device.DeviceListingActivity
import com.embeltech.meterreading.ui.device.model.User
import com.embeltech.meterreading.ui.device.model.newScreens.ResponseUserListItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject


class UserListFragment : BaseFragment() {
    companion object {
        fun newInstance() = UserListFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var deviceDetailsViewModel: DeviceDetailsViewModel

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private var userList: ArrayList<ResponseUserListItem> = ArrayList()
    private var isUpdate: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentComponent.inject(this)
        deviceDetailsViewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(DeviceDetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_user_list, container, false)

        deviceDetailsViewModel.getStatus().observe(viewLifecycleOwner) {
            handleStatus(it)
        }

        val fabCreateUser: FloatingActionButton = root.findViewById(R.id.fabCreateUserList)
        userRecyclerView = root.findViewById(R.id.usersRecyclerView)
        userRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        deviceDetailsViewModel.getAllDevices()
        fabCreateUser.setOnClickListener {
            startCreateDevice(null, false)
        }

        deviceDetailsViewModel.getDashboardUserList(appPreferences.getUserId(),appPreferences.getUserRole()!!)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // addUserViewModel = ViewModelProvider(this).get(AddUserViewModel::class.java)
        super.onActivityCreated(savedInstanceState)


    }

    private fun startCreateDevice(nothing: Nothing?, b: Boolean) {
        val intent = Intent(activity, UserListingActivity::class.java)
        startActivityForResult(intent, 1000)
    }

    private fun handleStatus(it: Status?) {
        when (it) {
            is GetDashboardUserList -> {
                if (it.userList.isNotEmpty()) {
                    Log.i("@Device", "list==========>" + it.userList.toString())
                    setListToAdapter(it.userList)

                }

            }
        }


    }

    private fun setListToAdapter(list: List<ResponseUserListItem>) {
            userAdapter = UserAdapter(requireContext(), list as java.util.ArrayList<ResponseUserListItem>, appPreferences.getUserRole()!!)
            userRecyclerView.adapter = userAdapter

    }

}
