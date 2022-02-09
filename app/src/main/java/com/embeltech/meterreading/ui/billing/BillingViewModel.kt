package com.embeltech.meterreading.ui.billing

import androidx.lifecycle.MutableLiveData
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.data.repository.BIRepository
import com.embeltech.meterreading.extensions.addToCompositeDisposable
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.BaseViewModel
import com.embeltech.meterreading.ui.billing.model.DeviceDataDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BillingViewModel @Inject constructor(private val repository: BIRepository) :
    BaseViewModel() {

    @Inject
    lateinit var appPreferences: AppPreferences

    /**
     * Observer status value
     */
    fun getStatus(): MutableLiveData<Status> {
        return status
    }

    fun getAllAmrIdList() {
        repository.getAmrIdList(
            appPreferences.getToken()!!,
            appPreferences.getUserId(),
            appPreferences.getUserRole()!!,
            "ble"
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = GetAMRIdList(it)
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getLastBillNumber(selectedAMRId: String) {
        repository.getLastBillNumber(
            appPreferences.getToken()!!
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = GetLastBillNumber(it)
                getDeviceDetailsAgainstAmrId(selectedAMRId)
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getTotalVolumeAgainstAMRId(
        selectedStartDate: String,
        selectedEndDate: String,
        selectedAMRId: String
    ) {
        repository.getTotalVolumeAgainstAMRId(
            appPreferences.getToken()!!, appPreferences.getUserId(), appPreferences.getUserRole()!!,
            selectedAMRId, selectedStartDate, selectedEndDate
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.postValue(GetTotalConsumption(it))
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getDeviceDetailsAgainstAmrId(
        selectedAMRId: String
    ) {
        repository.getDeviceDetailsAgainstAmrId(
            appPreferences.getToken()!!, appPreferences.getUserId(), appPreferences.getUserRole()!!,
            selectedAMRId
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = GetUserNameAndAddress(it)
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getBillingDetails(deviceDataDetail: DeviceDataDetail) {
        repository.getBillingDetails(
            appPreferences.getToken()!!, deviceDataDetail
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.postValue(BillingDataSavedSuccessfully(it))
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getBillingInvoice(billNumber: Long) {
        repository.getBillingInvoice(appPreferences.getToken()!!, billNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            })
            .addToCompositeDisposable(disposable)
    }

    fun getDeviceDetails(amrId:String) {
        repository.getDeviceDetails(appPreferences.getToken()!!,amrId,appPreferences.getUserId(), appPreferences.getUserRole()!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                //status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = GetDeviceDetails(it)
            }, {
                //status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            })
            .addToCompositeDisposable(disposable)
    }
}