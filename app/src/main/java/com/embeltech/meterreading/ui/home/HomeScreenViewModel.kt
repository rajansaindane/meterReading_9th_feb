package com.embeltech.meterreading.ui.home

import androidx.lifecycle.MutableLiveData
import com.embeltech.meterreading.config.Constants
import com.embeltech.meterreading.config.Constants.StatusMessages.DIA_LOG_EMPTY
import com.embeltech.meterreading.config.Constants.StatusMessages.DIA_LOG_SUCCESS
import com.embeltech.meterreading.data.database.model.MeterBeacon
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.data.repository.BIRepository
import com.embeltech.meterreading.extensions.addToCompositeDisposable
import com.embeltech.meterreading.livedata.*
import com.embeltech.meterreading.ui.BaseViewModel
import com.embeltech.meterreading.ui.billing.model.DeviceDataDetail
import com.embeltech.meterreading.utils.CSVLogWriter
import com.embeltech.meterreading.utils.LogDataParser
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

class HomeScreenViewModel @Inject constructor(private val repository: BIRepository) :
    BaseViewModel() {

    private var mLogWriter: CSVLogWriter? = null
    var diaLogFile: File? = null

    @Inject
    lateinit var appPreferences: AppPreferences

    /**
     * Observer status value
     */
    fun getStatus(): MutableLiveData<Status> {
        return status
    }

    // create Diagnostic Logs file and write headers
    private fun createMeterReadingHeaderWriting() {
        val filePath =
            Constants.CsvWriter.LOG_WRITER_DIR + Constants.CsvWriter.METER_READING_LOGGING + Constants.CsvWriter.CSV
        val dir = File(Constants.CsvWriter.LOG_WRITER_DIR)
        // create directory if not present
        if (!dir.exists()) {
            dir.mkdirs()
        }
        diaLogFile = File(filePath)
        if (diaLogFile!!.exists()) {
            diaLogFile?.delete()
        }

        try {
            // create file if not present
            diaLogFile!!.createNewFile()
            Timber.e("File created.")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // open log writer
        try {
            mLogWriter = CSVLogWriter.openWriter(filePath)
            Timber.e("CSV File writer open")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        // write headers to the file
        mLogWriter!!.writeHeaders(Constants.CsvWriter.DIA_LOG_HEADERS)
        Timber.e("Header write to the file")

        try {
            mLogWriter!!.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun exportData() {
        Observable.fromCallable {
            repository.getAllBeaconList()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({
                if (it.isNotEmpty()) {
                    writeLogsToFile(it)
                } else {
                    status.value = ExportStatus(DIA_LOG_EMPTY)
                }
            }, {
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    // write logs to the file if file present
    // if file not present then create new file and write headers.
    private fun writeLogsToFile(logs: List<MeterBeacon>?) {

        createMeterReadingHeaderWriting()

        Timber.e("mLogWriter $mLogWriter")
        if (mLogWriter != null) {
            try {
                mLogWriter!!.writeLog(getDiaLogStringArrayList(logs))
                status.value = ExportStatus(DIA_LOG_SUCCESS)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (mLogWriter != null) {
//                    mLogWriter!!.flush()
                    mLogWriter!!.closeWriter()
                }
            }
        }
    }

    private fun getDiaLogStringArrayList(logs: List<MeterBeacon>?): List<Array<String>> {
        return LogDataParser.parseDiagnosticData(logs)
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
                status.postValue(GetLastBillNumber(it))
                getDeviceDetailsAgainstAmrId(selectedAMRId)
            }, {
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.value = Failed(it.message!!)
            }).addToCompositeDisposable(disposable)
    }

    fun getDataSampleCount(selectedAMRId: String) {
        repository.getDataSampleCount(
            appPreferences.getToken()!!,
            appPreferences.getUserId(),
            appPreferences.getUserRole()!!,
            selectedAMRId
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { status.value = ShowProgressDialog(Constants.Progress.SHOW_PROGRESS) }
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.postValue(GetDataSampleCount(it.toLong()))
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

    private fun getDeviceDetailsAgainstAmrId(
        selectedAMRId: String
    ) {
        repository.getDeviceDetailsAgainstAmrId(
            appPreferences.getToken()!!, appPreferences.getUserId(), appPreferences.getUserRole()!!,
            selectedAMRId
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                status.value = ShowProgressDialog(Constants.Progress.HIDE_PROGRESS)
                status.postValue(GetUserNameAndAddress(it))
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
}