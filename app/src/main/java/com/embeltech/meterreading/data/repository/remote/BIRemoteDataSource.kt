package com.embeltech.meterreading.data.repository.remote

import com.embeltech.meterreading.data.api.BIService
import com.embeltech.meterreading.data.database.model.MeterBeacon
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.data.repository.BIDataSource
import com.embeltech.meterreading.ui.adduser.SignUpRequest
import com.embeltech.meterreading.ui.billing.model.DeviceDataDetail
import com.embeltech.meterreading.ui.billing.model.DeviceDetailsResponse
import com.embeltech.meterreading.ui.device.model.*
import com.embeltech.meterreading.ui.device.model.stat_model.StatisticResponsesItem
import com.embeltech.meterreading.ui.device.model.stat_model.TotalConsumptionResponse
import com.embeltech.meterreading.ui.login.model.LoginResponse
import com.embeltech.meterreading.ui.report.model.ReportResponse
import com.embeltech.meterreading.ui.report.model.ReportResponseItem
import com.embeltech.meterreading.ui.scanbeacon.model.BeaconPayload
import com.embeltech.meterreading.ui.statistics.model.StatisticResponseItem
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class BIRemoteDataSource @Inject constructor(private var bIService: BIService) :
    BIDataSource {

    @Inject
    lateinit var appPreferences: AppPreferences

    private fun prepareRequestBody(requestObject: Any): RequestBody {
        val data = Gson().toJson(requestObject)
        return RequestBody.create(MediaType.parse("application/json"), data)
    }

    private fun getToken() =
        appPreferences.getPreferences(AppPreferences.PREFS_KEY_TOKEN)

    override fun insertBeacon(beacon: MeterBeacon): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBeacon(beacon: MeterBeacon): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteBeacon(beacon: MeterBeacon): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBeaconInsertId(beaconId: Int): Long {
        TODO("not implemented")
    }

    override fun getAllBeaconList(): List<MeterBeacon> {
        TODO("Not yet implemented")
    }

    override fun saveDeviceToDatabase(device: Device): Long {
        TODO("Not yet implemented")
    }

    override fun getAllDevices(): Single<List<Device>> {
        TODO("Not yet implemented")
    }

    override fun login(email: String, password: String): Observable<LoginResponse> {
        return bIService.login(email, password)
    }

    override fun getUserList(token: String, userId: Long, role: String): Observable<List<User>> {
        return bIService.getUserList(token)
    }

    override fun getAdminList(
        token: String,
        userId: Long,
        userRole: String
    ): Observable<List<Admin>> {
        return bIService.getAdminList(token)
    }

    override fun createDevice(token: String, request: Device): Observable<SaveDeviceResponse> {
        return bIService.createDevice(token, request)
    }

    override fun getDeviceList(
        token: String,
        fkUserId: Long,
        role: String
    ): Observable<List<DeviceListResponse>> {
        return bIService.getDeviceList(token, fkUserId, role)
    }

    override fun getAmrIdList(
        token: String,
        fkUserId: Long,
        role: String,
        deviceType : String
    ): Observable<List<String>> {
        return bIService.getAmrIdList(token, fkUserId, role, deviceType)
    }

    override fun getLastBillNumber(token: String): Observable<Int> {
        return bIService.getLastBillNumber(token)
    }

    override fun getTotalVolumeAgainstAMRId(
        token: String,
        fkUserId: Long,
        role: String,
        amrId: String,
        fromDate: String,
        toDate: String
    ): Observable<List<String>> {
        return bIService.getTotalVolumeAgainstAMRId(token, fkUserId, role, amrId, fromDate, toDate)
    }

    override fun getDeviceDetailsAgainstAmrId(
        token: String,
        fkUserId: Long,
        role: String,
        amrId: String
    ): Observable<List<String>> {
        return bIService.getDeviceDetailsAgainstAmrId(token, fkUserId, role, amrId)
    }

    override fun getBillingDetails(
        token: String,
        deviceDataDetail: DeviceDataDetail
    ): Observable<String> {
        return bIService.getBillingDetails(token, deviceDataDetail)
    }

    override fun getBillingInvoice(token: String, billNumber: Long): Observable<String> {
        return bIService.getBillingInvoice(token, billNumber)
    }

    override fun deleteDevice(token: String, deviceId: ArrayList<Long>): Observable<String> {
        return bIService.deleteDevice(token, deviceId)
    }

    override fun updateDevice(token: String, request: Device) : Completable {
        return bIService.updateDevice(token, request)
    }

    override fun getDataSampleCount(
        token: String,
        fkUserId: Long,
        role: String,
        amrId: String
    ): Observable<Double> {
        return bIService.getDataSampleCount(token, fkUserId.toString(), role, amrId)
    }

    override fun getReportData(
        token: String,
        fromDate: String,
        toDate:String,
        fkUserId: Long,
        role: String
    ): Observable<List<ReportResponseItem>> {
        return bIService.getReportData(token,fromDate, toDate, fkUserId, role)
    }

    override fun saveBeaconData(token: String, beaconPayloads: List<BeaconPayload>): Observable<String> {
        return bIService.saveBeaconData(token,beaconPayloads)
    }

    override fun getStatisticData(
        token: String,
        fkUserId: Long,
        role: String
    ): Observable<List<StatisticResponseItem>> {
        return bIService.getStatisticData(token, fkUserId, role)
    }

    override fun getStatisticResponseData(
        token: String,
        duration: String,
        fromDate: String,
        toDate: String,
        fkUserId: Long,
        role: String
    ): Observable<List<StatisticResponsesItem>> {
        return bIService.getStatisticData(token, duration, fromDate, toDate, fkUserId, role)
    }

    override fun getDeviceDetails(
        token: String,
        amrid: String,
        fkUserId: Long,
        role: String
    ): Observable<DeviceDetailsResponse> {
        return bIService.getDeviceDetails(token, amrid, fkUserId, role)

    }

    override fun saveSignUpUser(token: String, signUpRequest: SignUpRequest): Observable<String> {
        return  bIService.saveSignUpUser(token, signUpRequest)
    }

    override fun getTotalConsumption(
        token: String,
        fkUserId: Long,
        role: String
    ): Observable<TotalConsumptionResponse> {
        return bIService.getTotalConsumption(token, fkUserId, role)
    }

    override fun getDurationReport(
        token: String,
        duration: String,
        fkUserId: Long,
        role: String
    ): Observable<List<ReportResponseItem>> {
        return bIService.getDurationReport(token, duration, fkUserId, role)
    }


}