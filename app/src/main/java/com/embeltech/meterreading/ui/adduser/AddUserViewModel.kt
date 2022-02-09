package com.embeltech.meterreading.ui.adduser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.data.repository.BIRepository
import com.embeltech.meterreading.extensions.addToCompositeDisposable
import com.embeltech.meterreading.livedata.Failed
import com.embeltech.meterreading.livedata.SaveSignUpUser
import com.embeltech.meterreading.livedata.ShowProgressDialog
import com.embeltech.meterreading.livedata.Status
import com.embeltech.meterreading.ui.BaseViewModel
import com.embeltech.meterreading.ui.device.model.Device
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddUserViewModel @Inject constructor(private val repository: BIRepository) :
    BaseViewModel() {

    @Inject
    lateinit var appPreferences: AppPreferences

    /**
     * Observer status value
     */
    fun getStatus(): MutableLiveData<Status> {
        return status
    }



}