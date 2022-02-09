package com.embeltech.meterreading.ui.billing.model

import com.google.gson.annotations.SerializedName

data class DeviceDetailsResponse(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("rate")
	val rate: Double? = null,

	@field:SerializedName("fkUserId")
	val fkUserId: Any? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("user")
	val user: String? = null
)
