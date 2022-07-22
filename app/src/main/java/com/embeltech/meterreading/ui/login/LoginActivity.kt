package com.embeltech.meterreading.ui.login

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.BaseActivity
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.extensions.hideSoftKey
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.main.MainActivity
import com.embeltech.meterreading.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // if (appPreferences.getToken()!=null)
        setContentView(R.layout.activity_login)
        activityComponent.inject(this)

        loginViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        loginViewModel.getStatus().observe(this) {
            handleStatus(it)
        }
        login.setOnClickListener {
            userName.hideSoftKey()
            val sUserName = userName.text.toString()
            val sPassword = password.text.toString()
            if (sUserName.isNotEmpty()) {
                if (sPassword.isNotEmpty()) {
                    loginViewModel.sendLoginRequest(sUserName, sPassword)
                } else {
                    DialogUtils.showSnackBarMsg(
                        this,
                        relativeLogin,
                        "Please enter Password",
                        Snackbar.LENGTH_LONG
                    )
                }
            } else {
                DialogUtils.showSnackBarMsg(
                    this,
                    relativeLogin,
                    "Please enter username",
                    Snackbar.LENGTH_LONG
                )
            }
        }
        txtForgotPassword.setOnClickListener {
            forgotPassword()
        }
    }

    private fun forgotPassword() {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_forgot_password)
            val btnSubmit = dialog.findViewById(R.id.btnForgotSubmit) as Button
            val btnCancel = dialog.findViewById(R.id.btnForgotCancel) as Button
            val edtForgotEmail = dialog.findViewById(R.id.edtForgotEmail) as EditText


            btnSubmit.setOnClickListener {
                if (!edtForgotEmail.text.toString().isNullOrEmpty()
                    && !edtForgotEmail.text.toString().isNullOrEmpty()){
                    loginViewModel.getForgotPassword(edtForgotEmail.text.toString())
                    dialog.dismiss()
                }
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }


    private fun handleStatus(it: Status?) {
        when (it) {
            is ShowProgressDialog -> {
                when (it.message) {
                    Constants.Progress.SHOW_PROGRESS -> showProgressDialog()
                    Constants.Progress.HIDE_PROGRESS -> hideProgressDialog()
                }
            }
            is LoginSuccess -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                this.finish()
//                val intent = Intent(this, ScanBeaconActivity::class.java)
//                startActivity(intent)
            }
            is Failed -> {
                Log.i("@Login","error===>"+it.error)
                if (it.error.contains("404"))
                    DialogUtils.showOkAlert(this, "Meter Reading", "User not found.")
            }
            is GetForgotPassword -> {
                Toast.makeText(this, "RESET LINK SUBMITTED SUCCESSFULLY", Toast.LENGTH_SHORT).show()
            }
        }
    }
}