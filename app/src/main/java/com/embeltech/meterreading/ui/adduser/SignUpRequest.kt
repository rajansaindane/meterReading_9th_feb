package com.embeltech.meterreading.ui.adduser

import com.google.gson.annotations.SerializedName

data class SignUpRequest(

	@field:SerializedName("deviceType")
	var deviceType: String? = null,

	@field:SerializedName("firstname")
    var firstname: String? = null,

	@field:SerializedName("password")
	var password: String? = null,

	@field:SerializedName("role")
	var role: String? = null,

	@field:SerializedName("email")
	var email: String? = null,

	@field:SerializedName("lastname")
	var lastname: String? = null,

	@field:SerializedName("contactNo")
	var contactNo: String? = null,

	@field:SerializedName("username")
	var username: String? = null
)
