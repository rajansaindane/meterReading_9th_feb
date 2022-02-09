package com.embeltech.meterreading.ui.report

import android.util.Log
import androidx.lifecycle.LiveData
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.data.repository.BIRepository
import com.embeltech.meterreading.extensions.addToCompositeDisposable
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.BaseViewModel
import com.embeltech.meterreading.ui.adduser.SignUpRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReportViewModel @Inject constructor(private val repository: BIRepository) :
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
        repository.getAllDevices()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                status.value = GetAllDeviceListFromDB(it)
            }
            .doOnError {
                status.value = Failed(it.message!!)
            }
            .subscribe()
    }

    fun getReportData(startDate: String, endDate: String) {
        repository.getReportData(appPreferences.getToken()!!,startDate,endDate, appPreferences.getUserId(),
            appPreferences.getUserRole()!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                Log.i("@report", "success response===>$it");
                status.value=GetReportData(it)
            }, {
                Log.i("@report","error response===>"+it.message);
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getStatisticData() {
        repository.getStatisticData(
            appPreferences.getToken()!!, appPreferences.getUserId(),
            appPreferences.getUserRole()!!
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                Log.i("@report", "success response===>$it");
                status.value = GetStatisticData(it)
            }, {
                Log.i("@report", "error response===>" + it.message);
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }


    fun saveSignUpUser(signUpRequest: SignUpRequest) {
        repository.saveSignUpUser(appPreferences.getToken()!!, signUpRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value=SaveSignUpUser(it)
            }, {
                status.value = Failed(it.message!!)
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
            }).addToCompositeDisposable(disposable)
    }

    fun getDurationData(duration:String) {
        repository.getDurationReport(appPreferences.getToken()!!,duration, appPreferences.getUserId(),
            appPreferences.getUserRole()!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                Log.i("@report", "success response===>$it");
                status.value=GetDurationData(it)
            }, {
                Log.i("@report","error response===>"+it.message);
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }
}