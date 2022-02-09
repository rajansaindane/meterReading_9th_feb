package com.embeltech.meterreading.data.repository

import com.embeltech.meterreading.data.database.model.MeterBeacon
import com.embeltech.meterreading.ui.adduser.SignUpRequest
import com.embeltech.meterreading.ui.billing.model.DeviceDataDetail
import com.embeltech.meterreading.ui.billing.model.DeviceDetailsResponse
import com.embeltech.meterreading.ui.device.model.Admin
import com.embeltech.meterreading.ui.device.model.Device
import com.embeltech.meterreading.ui.device.model.DeviceListResponse
import com.embeltech.meterreading.ui.device.model.User
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
import kotlin.collections.ArrayList

/**
 * Interface where all the data source functions are defined. BIRepository class decides what needs to be done when these
 * functions gets called from somewhere.
 */
interface BIDataSource {

    /**
     * Local sources
     *
     * */
    // Used to Make entry of Newly configured Beacon
    fun insertBeacon(beacon: MeterBeacon): Long

    // Updated beacon details in Beacon Table
    fun updateBeacon(beacon: MeterBeacon): Int

    // Deletes Beacon Entry from Beacon Table and returns deleted count
    fun deleteBeacon(beacon: MeterBeacon): Int

    // Used to check beacon entry with provided Beacon Id so that user can update beacon details using beacon Id
    fun getBeaconInsertId(beaconId: Int): Long

    fun getAllBeaconList(): List<MeterBeacon>

    fun saveDeviceToDatabase(device: Device): Long

    fun getAllDevices(): Single<List<Device>>

    /**
     * Remote sources
     *
     * */
    fun login(email: String, password: String): Observable<LoginResponse>
    fun getUserList(token: String, userId: Long, role: String): Observable<List<User>>
    fun getAdminList(token: String, userId: Long, userRole: String): Observable<List<Admin>>
    fun createDevice(token: String, request: Device): Observable<String>
    fun getDeviceList(
        token: String,
        fkUserId: Long,
        role: String
    ): Observable<List<DeviceListResponse>>

    fun getAmrIdList(token: String, fkUserId: Long, role: String, deviceType:String): Observable<List<String>>
    fun getLastBillNumber(token: String): Observable<Int>
    fun getTotalVolumeAgainstAMRId(
        token: String, fkUserId: Long,
        role: String,
        amrId: String,
        fromDate: String,
        toDate: String
    ): Observable<List<String>>

    fun getDeviceDetailsAgainstAmrId(
        token: String, fkUserId: Long,
        role: String,
        amrId: String
    ): Observable<List<String>>

    fun getBillingDetails(token: String, deviceDataDetail: DeviceDataDetail): Observable<String>
    fun getBillingInvoice(token: String, billNumber : Long): Observable<String>
    fun deleteDevice(token: String, deviceId: ArrayList<Long>) : Observable<String>
    fun updateDevice(token: String, request: Device): Completable
    fun getDataSampleCount(token: String,
                           fkUserId: Long,
                           role: String,
                           amrId: String) : Observable<Double>

    fun getReportData(token: String,
                      fromDate: String,
                      toDate:String,
                           fkUserId: Long,
                           role: String
                           ) : Observable<List<ReportResponseItem>>

    fun saveBeaconData(token: String,
                      beaconPayloads: List<BeaconPayload>
    ) : Observable<String>

    fun getStatisticData(token: String,
                      fkUserId: Long,
                      role: String
    ) : Observable<List<StatisticResponseItem>>

    fun getStatisticResponseData(token: String,
                                 duration: String,
                                 fromDate: String,
                                 toDate:String,
                                 fkUserId: Long,
                                 role: String
    ) : Observable<List<StatisticResponsesItem>>

    fun getDeviceDetails(token: String,
                         amrid:String,
                                 fkUserId: Long,
                                 role: String
    ) : Observable<DeviceDetailsResponse>

    fun saveSignUpUser(token: String,
                       signUpRequest: SignUpRequest
    ) : Observable<String>

    fun getTotalConsumption(token: String,
                         fkUserId: Long,
                         role: String
    ) : Observable<TotalConsumptionResponse>

    fun getDurationReport(token: String,
                         duration:String,
                         fkUserId: Long,
                         role: String
    ) : Observable<List<ReportResponseItem>>
}