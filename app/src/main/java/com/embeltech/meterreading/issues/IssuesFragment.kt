package com.embeltech.meterreading.issues

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
import com.embeltech.meterreading.livedata.GetIssueList
import com.embeltech.meterreading.livedata.Status
import com.embeltech.meterreading.ui.device.DeviceDetailsViewModel
import java.util.ArrayList
import javax.inject.Inject


class IssuesFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var deviceDetailsViewModel: DeviceDetailsViewModel

    private lateinit var issuesRecyclerView: RecyclerView
    private lateinit var issuesAdapter: IssuesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentComponent.inject(this)
        deviceDetailsViewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(DeviceDetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_issues, container, false)
        deviceDetailsViewModel.getStatus().observe(requireActivity(), {
            handleStatus(it)
        })

        issuesRecyclerView = root.findViewById(R.id.issuesRecyclerView)
        issuesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        deviceDetailsViewModel.getIssueList()

        return root;
    }

    private fun handleStatus(it: Status?) {
        when (it) {
            is GetIssueList -> {
                if (it.issueGetResponse.isNotEmpty()) {
                    Log.i("@Device", "list==========>" + it.issueGetResponse.toString())
                    setListToAdapter(it.issueGetResponse)

                }

            }
        }


    }

    private fun setListToAdapter(list: List<IssueGetResponseItem>) {
        issuesAdapter = IssuesAdapter(requireContext(), list as ArrayList<IssueGetResponseItem?> , appPreferences.getUserRole()!!)
        issuesRecyclerView.adapter = issuesAdapter

    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment IssuesFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            IssuesFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}