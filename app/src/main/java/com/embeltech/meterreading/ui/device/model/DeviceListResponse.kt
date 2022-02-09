package com.embeltech.meterreading.ui.device.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DeviceListResponse : Serializable {
    @SerializedName("pkDeviceDetails")
    val pkDeviceDetails: Long = 0

    @SerializedName("deviceImei")
    val deviceImei: String = ""

    @SerializedName("deviceAmrId")
    val deviceAmrId: String = ""

    @SerializedName("deviceSim")
    val deviceSim: String = ""

    @SerializedName("deviceImsi")
    val deviceImsi: String = ""

    @SerializedName("deviceAmrEnable")
    val deviceAmrEnable: Long = 0

    @SerializedName("deviceWakeupTime")
    val deviceWakeupTime: String = ""

    @SerializedName("deviceTimeZone")
    val deviceTimeZone:String = ""

    @SerializedName("deviceDataSampleCount")
    val deviceDataSampleCount: Long = 0

    @SerializedName("deviceLiterPerPulse")
    val deviceLiterPerPulse: Double = 0.0

    @SerializedName("deviceDateTime")
    val deviceDateTime: String = ""

    @SerializedName("deviceApplicationOfAmr")
    val deviceApplicationOfAmr: String = ""

    @SerializedName("deviceType")
    val deviceType: String = ""

    @SerializedName("deviceDiameterSize")
    val deviceDiameterSize: Long = 0

    @SerializedName("deviceState")
    val deviceState:String = ""

    @SerializedName("deviceCity")
    val deviceCity: String = ""

    @SerializedName("deviceArea")
    val deviceArea: String = ""

    @SerializedName("deviceZone")
    val deviceZone: String = ""

    @SerializedName("deviceUser")
    val deviceUser: String = ""

    @SerializedName("deviceLitPerPrice")
    val deviceLitPerPrice: Double = 0.0

    @SerializedName("deviceMeterLocation")
    val deviceMeterLocation: String = ""

    @SerializedName("deviceCustomerAddress")
    val deviceCustomerAddress:String = ""

    @SerializedName("deviceCustomerName")
    val deviceCustomerName: String = ""

    @SerializedName("deviceAxisEnable")
    val deviceAxisEnable: Long = 0

    @SerializedName("deviceBuildingNameOrWingName")
    val deviceBuildingNameOrWingName: String = ""

    @SerializedName("deviceFkUserId")
    val deviceFkUserId: Long = 0

    @SerializedName("deviceAdmin")
    val deviceAdmin: String = ""

    @SerializedName("deviceFkAdminId")
    val deviceFkAdminId: Long = 0

    @SerializedName("deviceTypeAmrOrBle")
    val deviceTypeAmrOrBle: String = ""

    @SerializedName("deviceMeterStartReading")
    val deviceMeterStartReading: Double = 0.0

    @SerializedName("deviceMACId")
    val deviceMACId : String = ""

    @SerializedName("deviceName")
    val deviceName : String = ""

    @SerializedName("deviceSerialNumber")
    val deviceSerialNumber : String = ""
}