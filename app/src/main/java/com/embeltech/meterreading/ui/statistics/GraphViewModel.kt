package com.embeltech.meterreading.ui.statistics

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.data.repository.BIRepository
import com.embeltech.meterreading.extensions.addToCompositeDisposable
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GraphViewModel @Inject constructor(private val repository: BIRepository) :
    BaseViewModel() {

   @Inject
   lateinit var appPreferences:AppPreferences

    /**
     * Observer status value
     */
    fun getStatus(): LiveData<Status> {
        return status
    }

    fun getStatisticData() {
        repository.getStatisticData(appPreferences.getToken()!!,appPreferences.getUserId(),
            appPreferences.getUserRole()!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                Log.i("@report", "success response===>$it");
                status.value= GetStatisticData(it)
            }, {
                Log.i("@report","error response===>"+it.message);
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }
}