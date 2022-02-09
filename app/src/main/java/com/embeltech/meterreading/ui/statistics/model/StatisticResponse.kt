package com.embeltech.meterreading.ui.statistics.model

import com.google.gson.annotations.SerializedName

data class StatisticResponse(

	@field:SerializedName("StatisticResponse")
	val statisticResponse: List<StatisticResponseItem?>? = null
)

data class StatisticResponseItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("pluseCount")
	val pluseCount: String? = null,

	@field:SerializedName("macAddress")
	val macAddress: String? = null,

	@field:SerializedName("rssi")
	val rssi: String? = null,

	@field:SerializedName("serialNumber")
	val serialNumber: String? = null,

	@field:SerializedName("pkBlePayloadDetails")
	val pkBlePayloadDetails: Int? = null,

	@field:SerializedName("beaconNname")
	val beaconNname: String? = null,

	@field:SerializedName("meterReading")
	val meterReading: String? = null,

	@field:SerializedName("batteryPercentage")
	val batteryPercentage: String? = null,

	@field:SerializedName("time")
	val time: String? = null
)
