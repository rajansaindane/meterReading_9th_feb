package com.embeltech.meterreading.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.data.repository.BIRepository
import com.embeltech.meterreading.extensions.addToCompositeDisposable
import com.embeltech.meterreading.livedata.Failed
import com.embeltech.meterreading.livedata.LoginSuccess
import com.embeltech.meterreading.livedata.ShowProgressDialog
import com.embeltech.meterreading.livedata.Status
import com.embeltech.meterreading.ui.BaseViewModel
import com.embeltech.meterreading.ui.login.model.LoginResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: BIRepository) :
    BaseViewModel() {

    @Inject
    lateinit var appPreferences: AppPreferences
    /**
     * Observer status value
     */
    fun getStatus(): LiveData<Status> {
        return status
    }

    fun sendLoginRequest(userName: String, password: String) {
        repository.login(userName, password)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                Log.i("@Login","succ===>"+it)
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                if (it.token.isNotEmpty() && it.perList != null) {
                    saveLoginInfo(it)
                    status.value = LoginSuccess(it!!)

                }
            }, {
                Log.i("@Login","error===>"+it)
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    private fun saveLoginInfo(it: LoginResponse?) {
        appPreferences.saveToken(it!!.token)
        appPreferences.saveUserId(it.perList!![0].fkUserID)
        appPreferences.saveUserRole(it.perList[0].role)
        appPreferences.saveUserName("${it.perList[0].firstname} ${it.perList[0].lastname}")
    }
}