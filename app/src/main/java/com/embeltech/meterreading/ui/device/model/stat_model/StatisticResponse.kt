package com.embeltech.meterreading.ui.device.model.stat_model

import com.google.gson.annotations.SerializedName

data class StatisticResponse(

	@field:SerializedName("StatisticResponse")
	val statisticResponse: List<StatisticResponsesItem?>? = null
)

data class StatisticResponsesItem(

	@field:SerializedName("pluseCount")
	val pluseCount: Double? = null,

	@field:SerializedName("literPerPulse")
	val literPerPulse: Double? = null,

	@field:SerializedName("macAddress")
	val macAddress: String? = null,

	@field:SerializedName("deviceOrBeaconName")
	val deviceOrBeaconName: String? = null,

	@field:SerializedName("batteryPercentage")
	val batteryPercentage: Any? = null

//	@field:SerializedName("pluseCount")
//	val pluseCount: Double? = null,
//
//	@field:SerializedName("literPerPulse")
//	val literPerPulse: Double? = null,
//
//	@field:SerializedName("macAddress")
//	val macAddress: String? = null,
//
//	@field:SerializedName("deviceOrBeaconName")
//	val deviceOrBeaconName: String? = null,
//
//	@field:SerializedName("batteryPercentage")
//	val batteryPercentage: String? = null

)
