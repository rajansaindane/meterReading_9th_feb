package com.embeltech.meterreading.data.repository

import com.embeltech.meterreading.data.database.model.MeterBeacon
import com.embeltech.meterreading.issues.IssueGetResponse
import com.embeltech.meterreading.issues.IssueGetResponseItem
import com.embeltech.meterreading.ui.adduser.SignUpRequest
import com.embeltech.meterreading.ui.billing.model.DeviceDataDetail
import com.embeltech.meterreading.ui.billing.model.DeviceDetailsResponse
import com.embeltech.meterreading.ui.device.model.*
import com.embeltech.meterreading.ui.device.model.newScreens.*
import com.embeltech.meterreading.ui.device.model.stat_model.StatisticResponsesItem
import com.embeltech.meterreading.ui.device.model.stat_model.TotalConsumptionResponse
import com.embeltech.meterreading.ui.login.model.LoginResponse
import com.embeltech.meterreading.ui.report.model.ReportResponse
import com.embeltech.meterreading.ui.report.model.ReportResponseItem
import com.embeltech.meterreading.ui.scanbeacon.model.BeaconPayload
import com.embeltech.meterreading.ui.scanbeacon.model.DeviceListByUserResponse
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
    fun createDevice(token: String, request: Device): Observable<SaveDeviceResponse>
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
                      beaconPayloads: BeaconPayload
    ) : Observable<String>

    fun getStatisticData(token: String,
                      fkUserId: Long,
                      role: String
    ) : Observable<List<StatisticResponseItem>>

    fun getStatisticResponseData(token: String,
                                 duration: String,

                                 fkUserId: Long,
                                 role: String,
                                 fromDate: String,
                                 toDate:String
    ) : Observable<List<StatisticResponsesItem>>

    fun getStatisticResponseDataTest(token: String,
                                 duration: String,
                                 fkUserId: Long,
                                 role: String,
                                 fromDate: String,
                                 toDate:String
    ) : Observable<List<ResponseNewStatisticsItem>>

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

    //New Screens
    fun getDashboardAdminList(token: String,
                     fkUserId: Long,
                     role: String
    ) : Observable<List<ResponseAdminListItem>>


    fun getDashboardUserList(token: String,
                            fkUserId: Long,
                            role: String
    ) : Observable<List<ResponseUserListItem>>

    fun getDashboardAlertCount(token: String,
                             fkUserId: Long,
                             role: String
    ) : Observable<ResponseAlertCount>

    fun getDashboardDeviceList(token: String,
                               fkUserId: Long,
                               role: String
    ) : Observable<List<ResponseDeviceListItem>>

    fun getTotalizerData(token: String,
                                 duration: String,
                                 fromDate: String,
                                 toDate:String,
                                 fkUserId: Long,
                                 role: String
    ) : Observable<ResponseTotalizer>

    fun getForgotPassword(
                               email: String
    ) : Observable<String>

    fun getDeviceConsumption(
        token: String,
        deviceName: String
    ) : Observable<ResponseDeviceConsumption>

    fun getIssueList(token: String,
                         fkUserId: Long,
                         role: String
    ) : Observable<List<IssueGetResponseItem>>

    fun getDeviceListByUser(token: String,
                     fkUserId: Long,
                     role: String
    ) : Observable<DeviceListByUserResponse>
}