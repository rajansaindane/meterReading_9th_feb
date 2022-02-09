package com.embeltech.meterreading.ui.report.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReportResponse(

	@SerializedName("ReportResponse")
	val reportResponse: List<ReportResponseItem?>? = null
): Serializable

data class ReportResponseItem(

	@SerializedName("date")
	val date: String? = null,

	@SerializedName("pluseCount")
	val pluseCount: String? = null,

	@SerializedName("macAddress")
	val macAddress: String? = null,

	@SerializedName("rssi")
	val rssi: String? = null,

	@SerializedName("serialNumber")
	val serialNumber: String? = null,

	@SerializedName("pkBlePayloadDetails")
	val pkBlePayloadDetails: Int? = null,

	@SerializedName("beaconNname")
	val beaconNname: String? = null,

	@SerializedName("meterReading")
	val meterReading: String? = null,

	@SerializedName("batteryPercentage")
	val batteryPercentage: String? = null,

	@SerializedName("time")
	val time: String? = null
) :Serializable
