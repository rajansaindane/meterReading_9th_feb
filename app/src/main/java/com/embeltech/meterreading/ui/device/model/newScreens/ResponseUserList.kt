package com.embeltech.meterreading.ui.device.model.newScreens

import com.google.gson.annotations.SerializedName

data class ResponseUserList(

	@field:SerializedName("ResponseUserList")
	val responseUserList: List<ResponseUserListItem?>? = null
)

data class ResponseUserListItem(

	@field:SerializedName("deviceType")
	val deviceType: String? = null,

	@field:SerializedName("firstname")
	val firstname: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("pkUserDetails")
	val pkUserDetails: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("lastname")
	val lastname: String? = null,

	@field:SerializedName("contactNo")
	val contactNo: Long? = null,

	@field:SerializedName("username")
	val username: String? = null
)
