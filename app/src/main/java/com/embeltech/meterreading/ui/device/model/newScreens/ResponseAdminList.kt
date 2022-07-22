package com.embeltech.meterreading.ui.device.model.newScreens

import com.google.gson.annotations.SerializedName
import java.io.Serializable

 class ResponseAdminList : Serializable {

	@field:SerializedName("ResponseAdminList")
	val responseAdminList: List<ResponseAdminListItem?>? = null
}

 class ResponseAdminListItem : Serializable {

	@field:SerializedName("firstname")
	val firstname: String? = null

	@field:SerializedName("fkAdminId")
	val fkAdminId: Int? = null

	@field:SerializedName("lastname")
	val lastname: String? = null
}
