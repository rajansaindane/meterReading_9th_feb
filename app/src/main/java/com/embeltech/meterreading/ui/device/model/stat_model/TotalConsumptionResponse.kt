package com.embeltech.meterreading.ui.device.model.stat_model

import com.google.gson.annotations.SerializedName

data class TotalConsumptionResponse(

	@field:SerializedName("lTotalConsumption")
	val lTotalConsumption: Int? = null
)
