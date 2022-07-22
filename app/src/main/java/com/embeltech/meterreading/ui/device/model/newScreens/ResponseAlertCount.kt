package com.embeltech.meterreading.ui.device.model.newScreens

import com.google.gson.annotations.SerializedName

data class ResponseAlertCount(

	@field:SerializedName("totalizer")
	val totalizer: Int? = null,

	@field:SerializedName("adminUser")
	val adminUser: String? = null,

	@field:SerializedName("userCount")
	val userCount: String? = null,

	@field:SerializedName("totalMeters")
	val totalMeters: Int? = null,

	@field:SerializedName("feedbackCount")
	val feedbackCount: Int? = null,

	@field:SerializedName("contactusCount")
	val contactusCount: String? = null,

	@field:SerializedName("nonWorkingMeters")
	val nonWorkingMeters: Int? = null,

	@field:SerializedName("workingMeters")
	val workingMeters: Int? = null
)
