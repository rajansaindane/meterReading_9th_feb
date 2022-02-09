package com.embeltech.meterreading.ui.adduser

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.BaseFragment
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.livedata.SaveSignUpUser
import com.embeltech.meterreading.livedata.ShowProgressDialog
import com.embeltech.meterreading.livedata.Status
import com.embeltech.meterreading.ui.device.model.Device
import com.embeltech.meterreading.ui.main.MainActivity
import com.embeltech.meterreading.ui.report.ReportViewModel
import com.embeltech.meterreading.ui.scanbeacon.ScanBeaconActivity
import com.embeltech.meterreading.ui.statistics.GraphFragment
import kotlinx.android.synthetic.main.add_user_fragment.*
import javax.inject.Inject

class AddUserFragment : BaseFragment() {
    companion object {
        fun newInstance() = AddUserFragment()
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addUserViewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentComponent.inject(this)
        addUserViewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(ReportViewModel::class.java)
        val root = inflater.inflate(R.layout.add_user_fragment, container, false)

        addUserViewModel.getStatus().observe(viewLifecycleOwner) {
            handleStatus(it)
        }

        val btn=root.findViewById<Button>(R.id.saveUser)

        btn.setOnClickListener {
            var firstName=firstname.text.toString()
            var lastname=lastname.text.toString()
            var email=email.text.toString()
            var contactNo=contactNo.text.toString()
            var password=password.text.toString()
            var confirmPassword=confirmPassword.text.toString()
            var role=role.selectedItem.toString()
            var username=username.text.toString()
            var deviceType=deviceType.selectedItem.toString()

            if (firstName.isNullOrEmpty()){
                Toast.makeText(context, "Enter First Name", Toast.LENGTH_SHORT).show()
            }
            else if (lastname.isNullOrEmpty()){
                Toast.makeText(context, "Enter Last Name", Toast.LENGTH_SHORT).show()
            }
            else if (email.isNullOrEmpty()){
                Toast.makeText(context, "Enter email", Toast.LENGTH_SHORT).show()
            }
            else if(contactNo.isNullOrEmpty()){
                Toast.makeText(context, "Enter Contact Details", Toast.LENGTH_SHORT).show()

            }

                else if (contactNo.length in 1..9){
                    Toast.makeText(context, "Enter Valid Contact Number", Toast.LENGTH_SHORT).show()
                }



            else if (password.isNullOrEmpty()){
                if (password.length<6){
                    Toast.makeText(context, "Password should be min 8 AlphaNumeric", Toast.LENGTH_SHORT).show()
                }

            }
            else if(password != confirmPassword){
                Toast.makeText(context, "Confirm Password does not matched", Toast.LENGTH_SHORT).show()
            }
            else if (role.isNullOrEmpty()){
                Toast.makeText(context, "Enter role", Toast.LENGTH_SHORT).show()
            }
            else if (username.isNullOrEmpty()){
                Toast.makeText(context, "Enter username", Toast.LENGTH_SHORT).show()
            }
            else if (deviceType.isNullOrEmpty()){
                Toast.makeText(context, "Enter Device Type", Toast.LENGTH_SHORT).show()
            }
            else{
                val signUpRequest = SignUpRequest()
                signUpRequest.firstname=firstName
                signUpRequest.lastname=lastname
                signUpRequest.email=email
                signUpRequest.contactNo=contactNo
                signUpRequest.password=password
                signUpRequest.role=role
                signUpRequest.username=username
                signUpRequest.deviceType=deviceType
                addUserViewModel.saveSignUpUser(signUpRequest)
            }

        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
       // addUserViewModel = ViewModelProvider(this).get(AddUserViewModel::class.java)
        super.onActivityCreated(savedInstanceState)




    }


    private fun handleStatus(it: Status?) {
        when (it) {
            is SaveSignUpUser -> {
                Toast.makeText(context, "User Added Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

}