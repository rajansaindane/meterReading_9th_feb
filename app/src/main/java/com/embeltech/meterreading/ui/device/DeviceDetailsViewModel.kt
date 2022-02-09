package com.embeltech.meterreading.ui.device

import androidx.lifecycle.LiveData
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.data.repository.BIRepository
import com.embeltech.meterreading.extensions.addToCompositeDisposable
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class DeviceDetailsViewModel @Inject constructor(private val repository: BIRepository) :
    BaseViewModel() {

    @Inject
    lateinit var appPreferences: AppPreferences

    /**
     * Observer status value
     */
    fun getStatus(): LiveData<Status> {
        return status
    }

    fun getAllDevices() {
        repository.getDeviceList(
            appPreferences.getToken()!!,
            appPreferences.getUserId(),
            appPreferences.getUserRole()!!
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                if (it.isNotEmpty()) {
                    status.value = DeviceList(it)
                } else {
                    status.value = Failed("Data not available")
                }
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getUserList() {
        repository.getUserList(appPreferences.getToken()!!, appPreferences.getUserId(),
            appPreferences.getUserRole()!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS)}
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = GetUserList(it)
            },{
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
            }).addToCompositeDisposable(disposable)
    }

    fun getStatisticsResponseData(duration:String, fromDate: String, toDate:String) {
        repository.getStatisticResponseData(
            appPreferences.getToken()!!,
            duration,
            fromDate,
            toDate,
            appPreferences.getUserId(),
            appPreferences.getUserRole()!!
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                if (it.isNotEmpty()) {
                    status.value = GetStatisticDataResponse(it)
                } else {
                    status.value = Failed("Data not available")
                }
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getTotalConsumption() {
        repository.getTotalConsumption(
            appPreferences.getToken()!!,
            appPreferences.getUserId(),
            appPreferences.getUserRole()!!
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
               status.value=SaveTotalConsumption(it)
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }
}