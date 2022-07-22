package com.embeltech.meterreading.issues

import com.google.gson.annotations.SerializedName

data class IssueGetResponse(

	@field:SerializedName("IssueGetResponse")
	val issueGetResponse: List<IssueGetResponseItem?>? = null
)

data class IssueGetResponseItem(

	@field:SerializedName("issueid")
	val issueid: Int? = null,

	@field:SerializedName("issue")
	val issue: String? = null,

	@field:SerializedName("contact")
	val contact: Long? = null,

	@field:SerializedName("fkUserId")
	val fkUserId: Int? = null,

	@field:SerializedName("mailId")
	val mailId: String? = null,

	@field:SerializedName("userName")
	val userName: String? = null
)
