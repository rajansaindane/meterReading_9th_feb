package com.embeltech.meterreading.data.api

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
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Interface where all the api used in Meter reading are defined
 */
interface BIService {

    /**
     * Api for sending login request
     */
    @POST("users/signin?")
    fun login(
        @Query("username") email: String,
        @Query("password") password: String
    ): Observable<LoginResponse>

    @POST("devicedetail")
    fun createDevice(
        @Header("Authorization") accessToken: String,
        @Body request: Device
    ): Observable<SaveDeviceResponse>

    @GET("device/get-user-list")
    fun getUserList(
        @Header("Authorization") accessToken: String
    ): Observable<List<User>>

    @GET("device/get-admin-list")
    fun getAdminList(
        @Header("Authorization") accessToken: String?
    ): Observable<List<Admin>>

    @GET("device/get-device_list?")
    fun getDeviceList(
        @Header("Authorization") accessToken: String,
        @Query("fkUserId") fkUserId: Long,
        @Query("role") role: String
    ): Observable<List<DeviceListResponse>>

    //    @GET("billing/get_device_amr_id_list_for_billing?")
    @GET("billing/get_device_amr_id_list_for_billing_for_ble?")
    fun getAmrIdList(
        @Header("Authorization") token: String, @Query("fkUserId") fkUserId: Long,
        @Query("role") role: String, @Query("device_type") deviceType: String
    ): Observable<List<String>>

    @GET("billing/get_last_bill_no_for_billing")
    fun getLastBillNumber(@Header("Authorization") token: String): Observable<Int>

    @GET("billing/get_total_volume_against_amr_id")
    fun getTotalVolumeAgainstAMRId(
        @Header("Authorization") token: String,
        @Query("fkUserId") fkUserId: Long,
        @Query("role") role: String,
        @Query("amrid") amrId: String,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): Observable<List<String>>

    @GET("billing/get_device_details_against_amr_id")
    fun getDeviceDetailsAgainstAmrId(
        @Header("Authorization") token: String,
        @Query("fkUserId") fkUserId: Long,
        @Query("role") role: String,
        @Query("amrid") amrId: String
    ): Observable<List<String>>

    @POST("billingdetails")
    fun getBillingDetails(
        @Header("Authorization") token: String,
        @Body deviceDataDetail: DeviceDataDetail
    ): Observable<String>

    @GET("billing/billPDF/{transID}")
    fun getBillingInvoice(
        @Header("Authorization") token: String,
        @Path("transID") transactionId: Long
    ): Observable<String>

    @POST("device/delete-device")
    fun deleteDevice(
        @Header("Authorization") token: String,
        @Body deviceId: ArrayList<Long>
    ): Observable<String>

    @PUT("device/update-device")
    fun updateDevice(
        @Header("Authorization") accessToken: String,
        @Body request: Device
    ): Completable

    @GET("device/get-liter-per-pulse-against-amr-id-for-ble")
    fun getDataSampleCount(
        @Header("Authorization") token: String,
        @Query("fkUserId") fkUserId: String,
        @Query("role") role: String,
        @Query("amrid") amrId: String
    ): Observable<Double>

    @GET("BlePayload/get-blepayload-report-between-two-date-for-filter")
    fun getReportData(
        @Header("Authorization") token: String,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("fkUserId") fkUserId: Long,
        @Query("role") role: String
    ): Observable<List<ReportResponseItem>>

    //http://13.232.54.246/iotmeter/BlePayload
    @POST("BlePayload")
    fun saveBeaconData(
        @Header("Authorization") accessToken: String,
        @Body beaconPayloads: BeaconPayload
    ): Observable<String>
    //get-Blepayload_report_for_current_date
    @GET("BlePayload/get-blepayload-report-between-two-date-for-filter")
    fun getStatisticData(
        @Header("Authorization") token: String,
        @Query("fkUserId") fkUserId: Long,
        @Query("role") role: String
    ): Observable<List<StatisticResponseItem>>

    @GET("blepayload/get-payload-duration-for-statistics")
    fun getStatisticData(
        @Header("Authorization") token: String,
        @Query("duration") duration: String,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("fkUserId") fkUserId: Long,
        @Query("role") role: String
    ): Observable<List<StatisticResponsesItem>>

    @GET("billing/get_device_details_against_amr_id_for_ble")
    fun getDeviceDetails(
        @Header("Authorization") token: String,
        @Query("amrid") toDate: String,
        @Query("fkUserId") fkUserId: Long,
        @Query("role") role: String
    ): Observable<DeviceDetailsResponse>

    @POST("users/signup")
    fun saveSignUpUser(
        @Header("Authorization") accessToken: String,
        @Body signUpRequest: SignUpRequest
    ): Observable<String>

    @GET("blepayload/get-Total-consumstion")
    fun getTotalConsumption(
        @Header("Authorization") token: String,
        @Query("fkUserId") fkUserId: Long,
        @Query("role") role: String
    ): Observable<TotalConsumptionResponse>

    @GET("blepayload/getblepayloadToview_duration")
    fun getDurationReport(
        @Header("Authorization") token: String,
        @Query("duration") duration: String,
        @Query("fkUserId") fkUserId: Long,
        @Query("role") role: String
    ): Observable<List<ReportResponseItem>>
}
