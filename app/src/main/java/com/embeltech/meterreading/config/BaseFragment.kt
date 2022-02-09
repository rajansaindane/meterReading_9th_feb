package com.embeltech.meterreading.config

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.embeltech.meterreading.config.Constants.Permission.GPS_ENABLE_REQUEST
import com.embeltech.meterreading.config.Constants.Permission.LOCATION_PERMISSION_REQUEST
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.eventbus.RxBus
import com.embeltech.meterreading.extensions.appComponent
import com.embeltech.meterreading.extensions.isConnected
import com.embeltech.meterreading.injection.FragmentComponent
import com.embeltech.meterreading.injection.module.FragmentModule
import com.embeltech.meterreading.ui.ConfigPersistantDelegate
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragment which holds all the common operations across all the Fragments.
 *
 */
open class BaseFragment : Fragment() {
    private val configPersistDelegate = ConfigPersistantDelegate()
    lateinit var fragmentComponent: FragmentComponent

    var progressDialogHUD: KProgressHUD? = null

    @Inject
    lateinit var appPreferences: AppPreferences

    var disposeBag: CompositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        configPersistDelegate.onCreate(activity?.baseContext!!, savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentComponent =
            (activity?.application)!!.appComponent.fragmentModule(FragmentModule(this))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        configPersistDelegate.onSaveInstanceState(outState)
    }


    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            activity?.applicationContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (!connectivityManager.isConnected) {
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        configPersistDelegate.onDestroy()
        disposeBag.clear()
        RxBus.unregister(this)
    }

    /**
     * Check for location permission status and return boolean value
     *
     * @return
     */
    fun checkLocationPermission(): Boolean {
        var status = false

        val locationPermission = ActivityCompat.checkSelfPermission(
            activity?.applicationContext!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (locationPermission == PackageManager.PERMISSION_GRANTED) {
            status = true
        }

        return status
    }

    /**
     * Ask for runtime location permission
     */
    fun requestLocationPermission() {

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION_REQUEST
        )
    }

    /**
     * Check if gps service is on or off
     *
     * @return
     */
    fun isGPSProvider(): Boolean {
        val provider = android.provider.Settings.Secure.getString(
            activity?.contentResolver,
            android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED
        )

        return provider.contains("gps")
    }

    /**
     * Restrict app attributes being touch by user
     */
    fun blockWindowTouch() {
        activity?.window!!.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    /**
     * Enables touch in application
     */
    fun allowWindowTouch() {
        activity?.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    /**
     * Used to Shows toasts throughout all fragments
     */
    fun showToast(text: String) {
        Toast.makeText(activity?.applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    /**
     * Method used to check if android device has Bluetooth LE feature
     */
    fun checkBleSupport(): Boolean {
        return activity?.packageManager!!.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    /**
     * request user for enable gps service
     *
     * @param context
     */
    fun displayLocationSettingsRequest(context: Context) {
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
                        status.startResolutionForResult(activity, GPS_ENABLE_REQUEST)
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
        progressDialogHUD = KProgressHUD.create(activity)
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
        if (progressDialogHUD!=null && progressDialogHUD!!.isShowing)
            progressDialogHUD!!.dismiss()
    }
}