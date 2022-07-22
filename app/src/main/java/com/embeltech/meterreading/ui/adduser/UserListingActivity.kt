package com.embeltech.meterreading.ui.adduser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.BaseActivity
import com.embeltech.meterreading.livedata.GetUserList
import com.embeltech.meterreading.livedata.SaveSignUpUser
import com.embeltech.meterreading.livedata.Status
import com.embeltech.meterreading.ui.device.DeviceDetailsViewModel
import com.embeltech.meterreading.ui.device.model.newScreens.ResponseUserListItem
import com.embeltech.meterreading.ui.main.MainActivity
import com.embeltech.meterreading.ui.report.ReportViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_create_device.*
import kotlinx.android.synthetic.main.activity_create_device.deviceType
import kotlinx.android.synthetic.main.add_user_fragment.*
import javax.inject.Inject

class UserListingActivity : BaseActivity() {
    companion object {
        fun newInstance() = AddUserFragment()
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addUserViewModel: ReportViewModel

    private var isUpdate: Boolean = false


    private fun handleStatus(it: Status?) {
        when (it) {
            is SaveSignUpUser -> {
                Toast.makeText(this, "User Added Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

   
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_listing)
        activityComponent.inject(this)
        back.setOnClickListener {
            onBackPressed()
            intent.putExtra("result", isUpdate)
        }
        addUserViewModel =
            ViewModelProviders.of(this, viewModelFactory)
                .get(ReportViewModel::class.java)

        addUserViewModel.getStatus().observe(this) {
            handleStatus(it)
        }

        val btn=findViewById<Button>(R.id.saveUser)

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
                Toast.makeText(this, "Enter First Name", Toast.LENGTH_SHORT).show()
            }
            else if (lastname.isNullOrEmpty()){
                Toast.makeText(this, "Enter Last Name", Toast.LENGTH_SHORT).show()
            }
            else if (email.isNullOrEmpty()){
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
            }
            else if(contactNo.isNullOrEmpty()){
                Toast.makeText(this, "Enter Contact Details", Toast.LENGTH_SHORT).show()

            }

            else if (contactNo.length in 1..9){
                Toast.makeText(this, "Enter Valid Contact Number", Toast.LENGTH_SHORT).show()
            }



            else if (password.isNullOrEmpty()){
                if (password.length<6){
                    Toast.makeText(this, "Password should be min 8 AlphaNumeric", Toast.LENGTH_SHORT).show()
                }

            }
            else if(password != confirmPassword){
                Toast.makeText(this, "Confirm Password does not matched", Toast.LENGTH_SHORT).show()
            }
            else if (role.isNullOrEmpty()){
                Toast.makeText(this, "Enter role", Toast.LENGTH_SHORT).show()
            }
            else if (username.isNullOrEmpty()){
                Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show()
            }
            else if (deviceType.isNullOrEmpty()){
                Toast.makeText(this, "Enter Device Type", Toast.LENGTH_SHORT).show()
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

    }

  



}