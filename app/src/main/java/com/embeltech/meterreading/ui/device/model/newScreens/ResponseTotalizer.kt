package com.embeltech.meterreading.ui.device.model.newScreens

import com.google.gson.annotations.SerializedName

data class ResponseTotalizer(

	@field:SerializedName("totalConsumption")
	val totalConsumption: String? = null
)
