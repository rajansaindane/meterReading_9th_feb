package com.embeltech.meterreading.ui.device

import android.util.Log
import androidx.lifecycle.LiveData
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.data.repository.BIRepository
import com.embeltech.meterreading.extensions.addToCompositeDisposable
import com.embeltech.meterreading.issues.IssueGetResponseItem
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.BaseViewModel
import com.embeltech.meterreading.ui.device.model.newScreens.ResponseNewStatisticsItem
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



    fun getStatisticsResponseData(duration:String,userId:Long,role:String,fromDate: String, toDate:String) {
        repository.getStatisticResponseData(
            appPreferences.getToken()!!,
            duration,
            userId,
            role,
            fromDate,
            toDate
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                if (it.isNotEmpty()) {
                    Log.i("@Device", "stat response ====>${it}")
                    status.value = GetStatisticDataResponse(it)
                } else {
                    status.value = Failed("Data not available")
                }
            }, {
                Log.i("@Device", "stat error response ====>$it")
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getStatisticsResponseDataTest(duration:String,userId:Long,role:String,fromDate: String, toDate:String) {
        repository.getStatisticResponseDataTest(
            appPreferences.getToken()!!,
            duration,
            userId,
            role,
            fromDate,
            toDate
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                if (it.isNotEmpty()) {
                    Log.i("@Device", "stat response ====>${it.toString()}")
                    status.value = GetStatisticDataResponseTest((it))
                } else {
                    status.value = Failed("Data not available")
                }
            }, {
                Log.i("@Device", "stat error response ====>$it")
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

    //New screens

    fun getDashboardAdminList() {
        repository.getDashboardAdminList(
            appPreferences.getToken()!!,
            appPreferences.getUserId(),
            appPreferences.getUserRole()!!
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value=GetDashboardAdminList(it)
                Log.i("@Device","admin list =====>"+it.toString())

            }, {
                Log.i("@Device","failed admin list =====>"+it!!.message)
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getDashboardUserList(adminId:Long,role:String) {
        if (adminId==0L) {
            repository.getDashboardUserList(
                appPreferences.getToken()!!,
                appPreferences.getUserId(),
                appPreferences.getUserRole()!!
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS)
                }
                .subscribe({
                    status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                    status.value = GetDashboardUserList(it)
                }, {
                    status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                    status.value = Failed(it.message!!)
                }).addToCompositeDisposable(disposable)
        }
        else{
            repository.getDashboardUserList(
                appPreferences.getToken()!!,
                adminId,
                role
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS)
                }
                .subscribe({
                    status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                    status.value = GetDashboardUserList(it)
                }, {
                    status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                    status.value = Failed(it.message!!)
                }).addToCompositeDisposable(disposable)
        }
    }

    fun getAlertCount() {
        repository.getDashboardAlertCount(
            appPreferences.getToken()!!,
            appPreferences.getUserId(),
            appPreferences.getUserRole()!!
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value=GetAlertCount(it)
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getDashboardDeviceList(userId:Long,role:String) {
        if (userId==0L) {
            repository.getDashboardDeviceList(
                appPreferences.getToken()!!,
                appPreferences.getUserId(),
                appPreferences.getUserRole()!!
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS)
                }
                .subscribe({
                    status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                    status.value = GetDashboardDeviceList(it)
                }, {
                    status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                    status.value = Failed(it.message!!)
                }).addToCompositeDisposable(disposable)
        }
        else{
            repository.getDashboardDeviceList(
                appPreferences.getToken()!!,
                userId,
                role
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS)
                }
                .subscribe({
                    status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                    status.value = GetDashboardDeviceList(it)
                }, {
                    status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                    status.value = Failed(it.message!!)
                }).addToCompositeDisposable(disposable)
        }
    }

    fun getTotalizerData(duration:String, fromDate: String, toDate:String,userId:Long,role:String) {
        repository.getTotalizerData(
            appPreferences.getToken()!!,
            duration,
            fromDate,
            toDate,
            userId,
            role
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                if (it!=null) {
                    status.value = GetTotalizerData(it)
                } else {
                    status.value = Failed("Data not available")
                }
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getDeviceConsumption(deviceName:String) {
        repository.getDeviceConsumption(
            appPreferences.getToken()!!,
           deviceName
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                if (it!=null) {
                    status.value = GetDeviceConsumption(it.totalConsumption!!)
                } else {
                    status.value = Failed("Data not available")
                }
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getIssueList() {
            repository.getIssueList(
                appPreferences.getToken()!!,
                appPreferences.getUserId(),
                appPreferences.getUserRole()!!
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
                .subscribe({
                    status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                    status.value=GetIssueList(it)
                    Log.i("@Device","admin list =====>"+it.toString())

                }, {
                    Log.i("@Device","failed admin list =====>"+it!!.message)
                    status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                    status.value = Failed(it.message!!)
                }).addToCompositeDisposable(disposable)
        }

}