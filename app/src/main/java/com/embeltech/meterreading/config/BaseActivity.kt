package com.embeltech.meterreading.config

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.Constants.Permission.LOCATION_PERMISSION_REQUEST
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.eventbus.RxBus
import com.embeltech.meterreading.injection.ActivityComponent
import com.embeltech.meterreading.injection.module.ActivityModule
import com.embeltech.meterreading.ui.ConfigPersistantDelegate
import com.embeltech.meterreading.utils.DialogUtils
import com.embeltech.meterreading.utils.Utility
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.material.snackbar.Snackbar
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

/**
 * Activity which holds all the common operations across all the Activities.
 *
 */
abstract class BaseActivity : AppCompatActivity() {

    private val LOCATION_RUNTIME_PERMISSION_REQUEST = 1003
    private val STORAGE_RUNTIME_PERMISSION_REQUEST = 1005
    private val CAMERA_RUNTIME_PERMISSION_REQUEST = 1006
    private val BLUETOOTH_PERMISSION_REQUEST = 1001
//    val LOCATION_PERMISSION_REQUEST = 1002

    var progressDialogHUD: KProgressHUD? = null
    private val configPersistDelegate = ConfigPersistantDelegate()
    lateinit var activityComponent: ActivityComponent
    var disposeBag = CompositeDisposable()

    @Inject
    lateinit var appPreferences: AppPreferences

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        //configPersistDelegate.onCreate(this, savedInstanceState)
        //activityComponent = configPersistDelegate.component + ActivityModule(this)

        activityComponent = (application as Application).appComponent
            .activityModule(ActivityModule(this))

        val window: Window = this.window

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_700)
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        configPersistDelegate.onSaveInstanceState(outState)
    }

    fun checkSettingsEnabled(): Boolean {
        return if (Utility.isMarshMallow) {
            checkLocationSettings() && checkBluetoothSettings() && checkStoragePermission()
        } else {
            checkBluetoothSettings() && checkStoragePermission()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        configPersistDelegate.onDestroy()
        RxBus.unregister(this)
        disposeBag.clear()

    }

    fun showNoConnectivityError() {
        showSnack(getCoordinatorLayout(), R.string.no_connectivity, Snackbar.LENGTH_SHORT)
    }

    fun showUnknownError() {
        // showSnack(getCoordinatorLayout(), R.string.unknown_error, Snackbar.LENGTH_SHORT)
    }

    /**
     * Shows Snackbar without action throughout the activity
     */
    fun showSnack(
        parent: ViewGroup, messageResId: Int, length: Int,
        actionLabelResId: Int? = null, action: ((View) -> Unit)? = null,
        callback: ((Snackbar) -> Unit)? = null
    ) {
        showSnack(
            parent,
            getString(messageResId),
            length,
            actionLabelResId?.let { getString(it) },
            action,
            callback
        )
    }

    /**
     * Shows Snackbar with an action
     */
    fun showSnack(
        parent: ViewGroup, message: String, length: Int,
        actionLabel: String? = null, action: ((View) -> Unit)? = null,
        callback: ((Snackbar) -> Unit)? = null
    ) {
        val snack = Snackbar.make(parent, message, length)
            .apply {
                if (actionLabel != null) {
                    setAction(actionLabel, action)
                }
            }
        customizeSnackbar(this, snack)
        snack.show()

    }

    private fun customizeSnackbar(context: Context, snackbar: Snackbar) {
        snackbar.setActionTextColor(Color.WHITE)
        val sbView = snackbar.view
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200))

    }

    /**
     * Shows Snackbar with message only
     */
    fun showSnackMsg(string: String?) {
        showSnack(getCoordinatorLayout(), string!!, Snackbar.LENGTH_SHORT)
    }

    /**
     * Check required runtime permissions
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun checkRuntimePermission(permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            var msg: String? = null
            var permissions = arrayOfNulls<String>(0)
            var permissionRequest = 0
            when (permission) {
                ACCESS_FINE_LOCATION -> {
                    msg = resources.getString(R.string.location_runtime_permission_required)
                    permissions = arrayOf(
                        ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    permissionRequest = LOCATION_RUNTIME_PERMISSION_REQUEST
                }
                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    msg = resources.getString(R.string.string_storage_permission)
                    permissions = arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    permissionRequest = STORAGE_RUNTIME_PERMISSION_REQUEST
                }
                Manifest.permission.CAMERA -> {
                    msg = resources.getString(R.string.camera_runtime_permission_required)
                    permissions = arrayOf(Manifest.permission.CAMERA)
                    permissionRequest = CAMERA_RUNTIME_PERMISSION_REQUEST
                }

            }

            if (!shouldShowRequestPermissionRationale(permission)) {
                showPermissionDialog(msg!!, permissions, permissionRequest)
                return true
            }
            ActivityCompat.requestPermissions(this@BaseActivity, permissions, permissionRequest)
            return true
        }
        return false
    }

    /**
     * Shows Dialog to user regarding why permission is required in app functioning
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun showPermissionDialog(
        msg: String,
        permissions: Array<String?>,
        permissionRequest: Int
    ) {

        DialogUtils.showYesNoAlert(this,
            resources.getString(R.string.permission_required),
            msg,
            android.R.string.ok,
            android.R.string.cancel,
            object : DialogUtils.OnDialogYesNoActionListener {

                override fun onYesClick() {
                    ActivityCompat.requestPermissions(
                        this@BaseActivity,
                        permissions,
                        permissionRequest
                    )
                }

                override fun onNoClick() {
                    onRuntimePermissionDenied(permissionRequest)
                }
            })
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun onRuntimePermissionDenied(permissionRequest: Int) {
        var msg: String? = null
        var permissions = arrayOfNulls<String>(0)
        when (permissionRequest) {
            LOCATION_RUNTIME_PERMISSION_REQUEST -> {
                msg = resources.getString(R.string.location_permission_denied)
                permissions = arrayOf(
                    ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
            STORAGE_RUNTIME_PERMISSION_REQUEST -> {
                msg = resources.getString(R.string.storage_runtime_permission_required)
                permissions =
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
            }
            CAMERA_RUNTIME_PERMISSION_REQUEST -> {
                msg = resources.getString(R.string.camera_runtime_permission_required)
                permissions = arrayOf(Manifest.permission.CAMERA)
            }
        }

        val finalPermissions = permissions
        DialogUtils.showSnackBarMsg(
            this,
            findViewById(android.R.id.content),
            msg!!,
            Snackbar.LENGTH_INDEFINITE,
            resources.getString(R.string.allow)
        ) { ActivityCompat.requestPermissions(this@BaseActivity, permissions, permissionRequest) }
    }

    /**
     * Checks for location settings and displays dialog asking to enable location settings
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun checkLocationSettings(): Boolean {
        if (!Utility.isLocationServiceEnabled(this)) {
            DialogUtils.showYesNoAlert(this,
                resources.getString(R.string.location_permission_title),
                resources.getString(R.string.location_enable_request),
                android.R.string.ok,
                android.R.string.cancel,
                object : DialogUtils.OnDialogYesNoActionListener {
                    override fun onYesClick() {
//                        fireLocationServiceIntent()
                        displayLocationSettingsRequest(applicationContext, this@BaseActivity)
                    }

                    override fun onNoClick() {
                        showLocationServiceRequiredMsg()
                    }
                })
            return false
        }
        return !checkRuntimePermission(ACCESS_FINE_LOCATION)
    }

    /**
     * Checks and shows dialog requesting to enable BLuetooth service
     */
    private fun checkBluetoothSettings(): Boolean {
        if (!Utility.isBluetoothSupported) {
            DialogUtils.showOkAlert(
                this,
                resources.getString(R.string.bluetooth_permission_title),
                resources.getString(R.string.bluetooth_not_supported)
            ) {
                //finish()
            }
        } else if (!Utility.isBluetoothEnabled) {
            DialogUtils.showYesNoAlert(this,
                resources.getString(R.string.bluetooth_permission_title),
                resources.getString(R.string.bluetooth_enable_request),
                android.R.string.ok,
                android.R.string.cancel,
                object : DialogUtils.OnDialogYesNoActionListener {
                    override fun onYesClick() {
                        fireBluetoothEnableIntent()
                    }

                    override fun onNoClick() {
                        showBluetoothRequiredMsg()
                    }
                })
            return false
        } else {
            onBluetoothEnabled()
        }
        return true
    }

    /**
     * Checks storage permission and return flag in Boolean value
     */
    fun checkStoragePermission(): Boolean {
        var status = false

        val storagePermission = ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (storagePermission == PackageManager.PERMISSION_GRANTED) {
            status = true
        }

        return status
    }

    /**
     * Shows Snackbar requesting to enable Bluetooth
     */
    private fun showBluetoothRequiredMsg() {
        DialogUtils.showSnackBarMsg(
            this,
            getCoordinatorLayout(),
            resources.getString(R.string.bluetooth_required_to_use_app),
            Snackbar.LENGTH_LONG,
            resources.getString(R.string.enable)
        ) { fireBluetoothEnableIntent() }
    }

    /**
     * Shows Snackbar requesting to enable location service
     */
    private fun showLocationServiceRequiredMsg() {
        DialogUtils.showSnackBarMsg(
            this,
            getCoordinatorLayout(),
            resources.getString(R.string.location_service_required),
            Snackbar.LENGTH_LONG,
            resources.getString(R.string.enable)
        ) {
            displayLocationSettingsRequest(applicationContext, this)
        }
    }

    /**
     * Shows Snackbar notifying user about Bluetooth Enabled
     */
    private fun fireBluetoothEnableIntent() {

        Utility.enableBluetooth()
        DialogUtils.showSnackBarMsg(
            this,
            getCoordinatorLayout(),
            resources.getString(R.string.bluetooth_enabled),
            Snackbar.LENGTH_SHORT
        )
        onBluetoothEnabled()
    }

    /**
     * Take action to request location service
     */
    internal fun fireLocationServiceIntent() {
        val locationServiceIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(
            locationServiceIntent,
            LOCATION_PERMISSION_REQUEST
        )
    }

    /**
     * Method will be called after actions completed like Bluetooth and Location service requests
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            BLUETOOTH_PERMISSION_REQUEST -> if (resultCode == RESULT_OK) {
                DialogUtils.showSnackBarMsg(
                    this,
                    getCoordinatorLayout(),
                    resources.getString(R.string.bluetooth_enabled),
                    Snackbar.LENGTH_SHORT
                )
                //onBluetoothEnabled()

            } else {
                DialogUtils.showSnackBarMsg(
                    this,
                    getCoordinatorLayout(),
                    resources.getString(R.string.bluetooth_enabled_failed),
                    Snackbar.LENGTH_LONG,
                    resources.getString(R.string.enable)
                ) { fireBluetoothEnableIntent() }
            }
            LOCATION_PERMISSION_REQUEST -> if (resultCode == RESULT_OK || Utility.isLocationServiceEnabled(
                    this
                )
            ) {
                showLocationEnabledMsgAndCheckBluetooth()
            } else {
                DialogUtils.showSnackBarMsg(
                    this,
                    getCoordinatorLayout(),
                    resources.getString(R.string.location_enabled_failed),
                    Snackbar.LENGTH_LONG,
                    resources.getString(R.string.enable)
                ) {
                    displayLocationSettingsRequest(applicationContext, this)
                }
            }
            else -> {
            }
        }
    }

    /**
     * Shows Snackbar notifying user that location serivce is enabled
     */
    private fun showLocationEnabledMsgAndCheckBluetooth() {
        DialogUtils.showSnackBarMsg(
            this,
            getCoordinatorLayout(),
            resources.getString(R.string.location_enabled),
            Snackbar.LENGTH_SHORT
        )
        if (!checkRuntimePermission(ACCESS_FINE_LOCATION)) {
            if (checkBluetoothSettings()) {
                onBluetoothEnabled()
            }
        }
    }

    /**
     * Provides android default layout to show Snackbar
     */
    fun getCoordinatorLayout(): ViewGroup {
        return findViewById(android.R.id.content)
    }

    fun onBluetoothEnabled() {

    }

    /**
     * Method used to provide apps version
     */
    fun getAppVersionName(): String {
        val packageManager: PackageManager = packageManager
        var packageInfo: PackageInfo? = null

        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return packageInfo!!.versionName
    }

    /**
     * Used to to show Toast from anywhere in Activity scope
     */
    fun showToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    /**
     * Restrict user to touch anywhere in app screen
     */
    fun blockWindowTouch() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    /**
     * Enable touch event anywhere in app screen
     */
    fun allowWindowTouch() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    /**
     * Check for location permission status and return boolean value
     *
     * @return
     */
    fun checkLocationPermission(): Boolean {
        var status = false

        val locationPermission = ActivityCompat.checkSelfPermission(
            applicationContext,
            ACCESS_FINE_LOCATION
        )

        if (locationPermission == PackageManager.PERMISSION_GRANTED) {
            status = true
        }

        return status
    }

    /**
     * Ask for runtime location permission
     */
    @SuppressLint("BinaryOperationInTimber")
    @RequiresApi(Build.VERSION_CODES.M)
    fun requestLocationPermission() {

        requestPermissions(
            arrayOf(
                ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST
        )
    }


    /**
     * Ask for runtime storage permission
     */
    @SuppressLint("BinaryOperationInTimber")
    @RequiresApi(Build.VERSION_CODES.M)
    fun requestStoragePermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            Constants.Permission.STORAGE_PERMISSION_REQUEST
        )
    }

    /**
     * Method used to check if android device has Bluetooth LE feature
     */
    fun checkBleSupport(): Boolean {
        return this.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    /**
     * Used to request user about Location enable permission with High accuracy so that
     * we can get user's actual location
     */
    fun displayLocationSettingsRequest(context: Context, activity: Activity) {
        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API).build()
        googleApiClient.connect()

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = (10000 / 2).toLong()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.SettingsApi.checkLocationSettings(
            googleApiClient,
            builder.build()
        )
        result.setResultCallback { result1 ->
            val status = result1.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    Timber.i("All location settings are satisfied.")
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Timber.i("Location settings are not satisfied. Show the user a dialog to upgrade location settings ")

                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(activity, LOCATION_PERMISSION_REQUEST)
                    } catch (e: IntentSender.SendIntentException) {
                        Timber.i("PendingIntent unable to execute request.")
                    }

                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Timber.i("Location settings are inadequate, and cannot be fixed here. Dialog not created.")
            }
        }
    }

    /**
     * Shows progress dialog
     * @message Message display on progress dialog
     */
    fun showProgressDialog() {
        progressDialogHUD = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)

        progressDialogHUD!!.show()
    }


    /**
     * Dismiss progress dialog
     */
    fun hideProgressDialog() {
        if (progressDialogHUD!!.isShowing)
            progressDialogHUD!!.dismiss()
    }
}