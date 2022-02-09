package com.embeltech.meterreading.livedata

import com.embeltech.meterreading.ui.adduser.SignUpRequest
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
import com.embeltech.meterreading.ui.statistics.model.StatisticResponseItem

sealed class Status

//Global
data class Failed(val error: String) : Status()

//show progress dialog
data class ShowProgressDialog(val message: Int) : Status()

data class DataSaveSuccessfully(val id: Long) : Status()

//show CSV Export status
data class ExportStatus(val message: String) : Status()

data class LoginSuccess(val loginResponse: LoginResponse) : Status()

data class GetAdminList(val admins : List<Admin>) : Status()
data class GetUserList(val users: List<User>) : Status()
object DeviceSavedSuccessfully : Status()

data class DeviceList(val list: List<DeviceListResponse>) : Status()

data class GetAllDeviceListFromDB(val devices : List<Device>) : Status()
data class GetAMRIdList(val amrIds : List<String>) : Status()
data class GetLastBillNumber(val billNumber : Int) : Status()
data class GetTotalConsumption(val consumptionUnits : List<String>) : Status()
data class GetUserNameAndAddress(val userNameAndAddress : List<String>) : Status()
data class BillingDataSavedSuccessfully(val message : String) : Status()
data class DeviceDeletedSuccessfully(val message: String) : Status()
data class GetDataSampleCount(val dataSampleCount : Long) : Status()
data class GetReportData(val reportDataList:List<ReportResponseItem>):Status()
data class SaveBeaconData(val message:String):Status()
data class GetStatisticData(val statisticData:List<StatisticResponseItem>):Status()
data class GetStatisticDataResponse(val statisticResponseData:List<StatisticResponsesItem>):Status()
data class GetDeviceDetails(val deviceDetails:DeviceDetailsResponse):Status()
data class SaveSignUpUser(val message:String):Status()
data class SaveTotalConsumption(val totalConsumption: TotalConsumptionResponse):Status()
data class GetDurationData(val reportDataList:List<ReportResponseItem>):Status()


