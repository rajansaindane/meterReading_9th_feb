package com.embeltech.meterreading.ui.device

import android.util.Log
import androidx.lifecycle.LiveData
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.data.repository.BIRepository
import com.embeltech.meterreading.extensions.addToCompositeDisposable
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.BaseViewModel
import com.embeltech.meterreading.ui.device.model.Device
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CreateDeviceViewModel @Inject constructor(private val repository: BIRepository) :
    BaseViewModel() {

    @Inject
    lateinit var appPreferences: AppPreferences

    /**
     * Observer status value
     */
    fun getStatus(): LiveData<Status> {
        return status
    }

    fun getUserList() {
        repository.getUserList(
            appPreferences.getToken()!!,
            appPreferences.getUserId(),
            appPreferences.getUserRole()!!
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = GetUserList(it)
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getAdminList() {
        repository.getAdminList(
            appPreferences.getToken()!!,
            appPreferences.getUserId(),
            appPreferences.getUserRole()!!
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = GetAdminList(it)
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun createDeviceRequest(request: Device) {
        repository.createDevice(appPreferences.getToken()!!, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
//                status.value = DeviceSavedSuccessfully
                Log.i("@Create","response===>"+it.present)
                saveDataToDatabase(request)
            }, {
                status.value = Failed(it.message!!)
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                Log.i("@Create","error esponse===>"+it.toString())

            }).addToCompositeDisposable(disposable)
    }

    private fun saveDataToDatabase(request: Device) {
        val id = repository.saveDeviceToDatabase(request)
        if (id > 0) {
            status.postValue(DeviceSavedSuccessfully)
        }
    }

    fun deleteDevice(pkDeviceDetails: Long) {
        val ids = ArrayList<Long>()
        ids.add(pkDeviceDetails)
        repository.deleteDevice(appPreferences.getToken()!!, ids)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = DeviceDeletedSuccessfully(it)
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            })
            .addToCompositeDisposable(disposable)
    }

    fun updateDeviceRequest(device: Device) {
        repository.updateDevice(appPreferences.getToken()!!, device)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.postValue(DeviceSavedSuccessfully)
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }
}