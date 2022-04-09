package com.embeltech.meterreading.data.repository

import android.util.Log
import com.embeltech.meterreading.data.database.model.MeterBeacon
import com.embeltech.meterreading.data.preferences.AppPreferences
import com.embeltech.meterreading.injection.annotations.Local
import com.embeltech.meterreading.injection.annotations.Remote
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
 * Class designed based on Repository design pattern responsible for DB or Remote operations based on the requirements.
 * It calls the BIRemoteDataSource's functions to handle the remote operations and TreelLocalDataSource's functions to handle
 * Local db operations.
 *
 */
class BIRepository @Inject constructor(
    @Local private val localDataSource: BIDataSource,
    @Remote private val remoteDataSource: BIDataSource
) :
    BIDataSource {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun insertBeacon(beacon: MeterBeacon): Long {
        return localDataSource.insertBeacon(beacon)
    }

    override fun updateBeacon(beacon: MeterBeacon): Int {
        return localDataSource.updateBeacon(beacon)
    }

    override fun deleteBeacon(beacon: MeterBeacon): Int {
        return localDataSource.deleteBeacon(beacon)
    }

    override fun getBeaconInsertId(beaconId: Int): Long {
        return localDataSource.getBeaconInsertId(beaconId)
    }

    override fun getAllBeaconList(): List<MeterBeacon> {
        return localDataSource.getAllBeaconList()
    }

    override fun saveDeviceToDatabase(device: Device): Long {
        return localDataSource.saveDeviceToDatabase(device)
    }

    override fun getAllDevices(): Single<List<Device>> {
        return localDataSource.getAllDevices()
    }

    override fun login(email: String, password: String): Observable<LoginResponse> {
        return remoteDataSource.login(email, password)
    }

    override fun getUserList(token: String, userId: Long, role: String): Observable<List<User>> {
        return remoteDataSource.getUserList(token, userId, role)
    }

    override fun getAdminList(
        token: String,
        userId: Long,
        userRole: String
    ): Observable<List<Admin>> {
        return remoteDataSource.getAdminList(
            token,
            appPreferences.getUserId(),
            appPreferences.getUserRole()!!
        )
    }

    override fun createDevice(token: String, request: Device): Observable<SaveDeviceResponse> {
        return remoteDataSource.createDevice(token, request)
    }

    override fun getDeviceList(
        token: String,
        fkUserId: Long,
        role: String
    ): Observable<List<DeviceListResponse>> {
        return remoteDataSource.getDeviceList(token, fkUserId, role)
    }

    override fun getAmrIdList(
        token: String,
        fkUserId: Long,
        role: String,
        deviceType : String
    ): Observable<List<String>> {
        return remoteDataSource.getAmrIdList(token, fkUserId, role, deviceType)
    }

    override fun getLastBillNumber(token: String): Observable<Int> {
        return remoteDataSource.getLastBillNumber(token)
    }

    override fun getTotalVolumeAgainstAMRId(
        token: String,
        fkUserId: Long,
        role: String,
        amrId: String,
        fromDate: String,
        toDate: String
    ): Observable<List<String>> {
        return remoteDataSource.getTotalVolumeAgainstAMRId(
            token,
            fkUserId,
            role,
            amrId,
            fromDate,
            toDate
        )
    }

    override fun getDeviceDetailsAgainstAmrId(
        token: String,
        fkUserId: Long,
        role: String,
        amrId: String
    ): Observable<List<String>> {
        return remoteDataSource.getDeviceDetailsAgainstAmrId(token, fkUserId, role, amrId)
    }

    override fun getBillingDetails(
        token: String,
        deviceDataDetail: DeviceDataDetail
    ): Observable<String> {
        return remoteDataSource.getBillingDetails(token, deviceDataDetail)
    }

    override fun getBillingInvoice(token: String, billNumber: Long): Observable<String> {
        return remoteDataSource.getBillingInvoice(token, billNumber)
    }

    override fun deleteDevice(token: String, deviceId: ArrayList<Long>): Observable<String> {
        return remoteDataSource.deleteDevice(token, deviceId)
    }

    override fun updateDevice(token: String, request: Device): Completable {
        return remoteDataSource.updateDevice(token, request)
    }

    override fun getDataSampleCount(
        token: String,
        fkUserId: Long,
        role: String,
        amrId: String
    ): Observable<Double> {
        return remoteDataSource.getDataSampleCount(token, fkUserId, role, amrId)
    }

    override fun getReportData(
        token: String,
        fromDate: String,
        toDate:String,
        fkUserId: Long,
        role: String
    ): Observable<List<ReportResponseItem>> {
       return remoteDataSource.getReportData(token,fromDate, toDate, fkUserId, role)
    }

    override fun saveBeaconData(token: String, beaconPayloads: BeaconPayload): Observable<String> {
        return remoteDataSource.saveBeaconData(token,beaconPayloads)
    }

    override fun getStatisticData(
        token: String,
        fkUserId: Long,
        role: String
    ): Observable<List<StatisticResponseItem>> {
        return remoteDataSource.getStatisticData(token, fkUserId, role)
    }

    override fun getStatisticResponseData(
        token: String,
        duration: String,
        fromDate: String,
        toDate:String,
        fkUserId: Long,
        role: String
    ): Observable<List<StatisticResponsesItem>> {
        return remoteDataSource.getStatisticResponseData(token, duration, fromDate, toDate, fkUserId, role)
    }

    override fun getDeviceDetails(
        token: String,
        amrid: String,
        fkUserId: Long,
        role: String
    ): Observable<DeviceDetailsResponse> {
        return remoteDataSource.getDeviceDetails(token, amrid, fkUserId, role)

    }

    override fun saveSignUpUser(token: String, signUpRequest: SignUpRequest): Observable<String> {
        return remoteDataSource.saveSignUpUser(token, signUpRequest)
    }

    override fun getTotalConsumption(
        token: String,
        fkUserId: Long,
        role: String
    ): Observable<TotalConsumptionResponse> {
        return remoteDataSource.getTotalConsumption(token, fkUserId, role)
    }

    override fun getDurationReport(
        token: String,
        duration: String,
        fkUserId: Long,
        role: String
    ): Observable<List<ReportResponseItem>> {
       return remoteDataSource.getDurationReport(token, duration, fkUserId, role)
    }
}