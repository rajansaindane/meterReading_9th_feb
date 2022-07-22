package com.embeltech.meterreading.ui.device.model.newScreens

import com.google.gson.annotations.SerializedName

data class ResponseDeviceList(

	@field:SerializedName("ResponseDeviceList")
	val responseDeviceList: List<ResponseDeviceListItem?>? = null
)

data class ResponseDeviceListItem(

	@field:SerializedName("deviceAdmin")
	val deviceAdmin: String? = null,

	@field:SerializedName("deviceArea")
	val deviceArea: String? = null,

	@field:SerializedName("manuFacturer")
	val manuFacturer: Any? = null,

	@field:SerializedName("deviceName")
	val deviceName: String? = null,

	@field:SerializedName("deviceDateTime")
	val deviceDateTime: String? = null,

	@field:SerializedName("deviceLitPerPrice")
	val deviceLitPerPrice: Double? = null,

	@field:SerializedName("deviceFkAdminId")
	val deviceFkAdminId: Int? = null,

	@field:SerializedName("deviceAmrEnable")
	val deviceAmrEnable: Int? = null,

	@field:SerializedName("deviceUser")
	val deviceUser: String? = null,

	@field:SerializedName("deviceApplicationOfAmr")
	val deviceApplicationOfAmr: String? = null,

	@field:SerializedName("deviceAmrId")
	val deviceAmrId: String? = null,

	@field:SerializedName("deviceAxisEnable")
	val deviceAxisEnable: Int? = null,

	@field:SerializedName("deviceCustomerName")
	val deviceCustomerName: String? = null,

	@field:SerializedName("deviceCustomerAddress")
	val deviceCustomerAddress: String? = null,

	@field:SerializedName("deviceType")
	val deviceType: String? = null,

	@field:SerializedName("deviceMACId")
	val deviceMACId: String? = null,

	@field:SerializedName("deviceSim")
	val deviceSim: String? = null,

	@field:SerializedName("deviceLiterPerPulse")
	val deviceLiterPerPulse: Double? = null,

	@field:SerializedName("deviceTypeAmrOrBle")
	val deviceTypeAmrOrBle: String? = null,

	@field:SerializedName("deviceWakeupTime")
	val deviceWakeupTime: String? = null,

	@field:SerializedName("deviceFkUserId")
	val deviceFkUserId: Int? = null,

	@field:SerializedName("deviceCity")
	val deviceCity: String? = null,

	@field:SerializedName("deviceRegistrationdate")
	val deviceRegistrationdate: String? = null,

	@field:SerializedName("deviceBuildingNameOrWingName")
	val deviceBuildingNameOrWingName: String? = null,

	@field:SerializedName("pkDeviceDetails")
	val pkDeviceDetails: Int? = null,

	@field:SerializedName("deviceSerialNumber")
	val deviceSerialNumber: String? = null,

	@field:SerializedName("deviceDataSampleCount")
	val deviceDataSampleCount: Int? = null,

	@field:SerializedName("deviceZone")
	val deviceZone: String? = null,

	@field:SerializedName("deviceDiameterSize")
	val deviceDiameterSize: String? = null,

	@field:SerializedName("deviceMeterStartReading")
	val deviceMeterStartReading: Double? = null,

	@field:SerializedName("deviceImei")
	val deviceImei: String? = null,

	@field:SerializedName("deviceImsi")
	val deviceImsi: String? = null,

	@field:SerializedName("deviceTimeZone")
	val deviceTimeZone: String? = null,

	@field:SerializedName("deviceState")
	val deviceState: String? = null,

	@field:SerializedName("deviceMeterLocation")
	val deviceMeterLocation: String? = null
)
