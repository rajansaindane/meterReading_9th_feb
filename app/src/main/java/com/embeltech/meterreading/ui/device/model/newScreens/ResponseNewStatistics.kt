package com.embeltech.meterreading.ui.device.model.newScreens

import com.google.gson.annotations.SerializedName

data class ResponseNewStatistics(

	@field:SerializedName("ResponseNewStatistics")
	val responseNewStatistics: List<ResponseNewStatisticsItem?>? = null
)

data class ResponseNewStatisticsItem(

	@field:SerializedName("pluseCount")
	val pluseCount: Any? = null,

	@field:SerializedName("literPerPulse")
	val literPerPulse: Double? = null,

	@field:SerializedName("macAddress")
	val macAddress: String? = null,

	@field:SerializedName("deviceOrBeaconName")
	val deviceOrBeaconName: String? = null,

	@field:SerializedName("batteryPercentage")
	val batteryPercentage: Any? = null,

	@field:SerializedName("totalconsumption")
	val totalconsumption: Any? = null
)
