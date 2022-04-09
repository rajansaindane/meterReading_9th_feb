package com.embeltech.meterreading.data.repository.local

import com.embeltech.meterreading.data.database.BeaconsDao
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
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Class to handle local db operations
 */
class BILocalDataSource @Inject constructor(var dao: BeaconsDao) : BIDataSource {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun insertBeacon(beacon: MeterBeacon): Long {
        return dao.insertBeacon(beacon)
    }

    override fun updateBeacon(beacon: MeterBeacon): Int {
        return dao.updateBeacon(beacon)
    }

    override fun deleteBeacon(beacon: MeterBeacon): Int {
        return dao.deleteBeacon(beacon)
    }

    override fun getBeaconInsertId(beaconId: Int): Long {
        return dao.getBeaconInsertId(beaconId)
    }

    override fun getAllBeaconList(): List<MeterBeacon> {
        return dao.getAllBeaconList()
    }

    override fun saveDeviceToDatabase(device: Device): Long {
        return dao.saveDevice(device)
    }

    override fun getAllDevices(): Single<List<Device>> {
        return dao.getAllDevices()
    }

    override fun login(email: String, password: String): Observable<LoginResponse> {
        TODO("Not yet implemented")
    }

    override fun getUserList(token: String, userId: Long, role: String): Observable<List<User>> {
        TODO("Not yet implemented")
    }

    override fun getAdminList(
        token: String,
        userId: Long,
        userRole: String
    ): Observable<List<Admin>> {
        TODO("Not yet implemented")
    }

    override fun createDevice(token: String, request: Device): Observable<SaveDeviceResponse> {
        TODO("Not yet implemented")
    }

    override fun getDeviceList(
        token: String,
        fkUserId: Long,
        role: String
    ): Observable<List<DeviceListResponse>> {
        TODO("Not yet implemented")
    }

    override fun getAmrIdList(
        token: String,
        fkUserId: Long,
        role: String,
        deviceType : String
    ): Observable<List<String>> {
        TODO("Not yet implemented")
    }

    override fun getLastBillNumber(token: String): Observable<Int> {
        TODO("Not yet implemented")
    }

    override fun getTotalVolumeAgainstAMRId(
        token: String,
        fkUserId: Long,
        role: String,
        amrId: String,
        fromDate: String,
        toDate: String
    ): Observable<List<String>> {
        TODO("Not yet implemented")
    }

    override fun getDeviceDetailsAgainstAmrId(
        token: String,
        fkUserId: Long,
        role: String,
        amrId: String
    ): Observable<List<String>> {
        TODO("Not yet implemented")
    }

    override fun getBillingDetails(
        token: String,
        deviceDataDetail: DeviceDataDetail
    ): Observable<String> {
        TODO("Not yet implemented")
    }

    override fun getBillingInvoice(token: String, billNumber: Long): Observable<String> {
        TODO("Not yet implemented")
    }

    override fun deleteDevice(token: String, deviceId: ArrayList<Long>): Observable<String> {
        TODO("Not yet implemented")
    }

    override fun updateDevice(token: String, request: Device): Completable {
        TODO("Not yet implemented")
    }

    override fun getDataSampleCount(
        token: String,
        fkUserId: Long,
        role: String,
        amrId: String
    ): Observable<Double> {
        TODO("Not yet implemented")
    }

    override fun getReportData(
        token: String,
        fromDate: String,
        toDate:String,
        fkUserId: Long,
        role: String
    ): Observable<List<ReportResponseItem>> {
        TODO("Not yet implemented")
    }

    override fun saveBeaconData(token: String, beaconPayloads: BeaconPayload): Observable<String> {
        TODO("Not yet implemented")
    }

    override fun getStatisticData(
        token: String,
        fkUserId: Long,
        role: String
    ): Observable<List<StatisticResponseItem>> {
        TODO("Not yet implemented")
    }


    override fun getStatisticResponseData(
        token: String,
        duration: String,
        fromDate: String,
        toDate: String,
        fkUserId: Long,
        role: String
    ): Observable<List<StatisticResponsesItem>> {
        TODO("Not yet implemented")
    }

    override fun getDeviceDetails(
        token: String,
        amrid: String,
        fkUserId: Long,
        role: String
    ): Observable<DeviceDetailsResponse> {
        TODO("Not yet implemented")
    }

    override fun saveSignUpUser(token: String, signUpRequest: SignUpRequest): Observable<String> {
        TODO("Not yet implemented")
    }

    override fun getTotalConsumption(
        token: String,
        fkUserId: Long,
        role: String
    ): Observable<TotalConsumptionResponse> {
        TODO("Not yet implemented")
    }

    override fun getDurationReport(
        token: String,
        duration: String,
        fkUserId: Long,
        role: String
    ): Observable<List<ReportResponseItem>> {
        TODO("Not yet implemented")
    }
}