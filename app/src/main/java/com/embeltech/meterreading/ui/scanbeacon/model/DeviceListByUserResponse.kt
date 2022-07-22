package com.embeltech.meterreading.ui.scanbeacon.model

import com.google.gson.annotations.SerializedName

data class DeviceListByUserResponse(

	@field:SerializedName("timeStamp")
	val timeStamp: String? = null,

	@field:SerializedName("scanedMeters")
	val scanedMeters: Int? = null,

	@field:SerializedName("notScanedMeters")
	val notScanedMeters: Int? = null
)
