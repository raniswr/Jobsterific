package com.example.jobsterific.data.response

import com.google.gson.annotations.SerializedName

data class EditCompanyProfileResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("customer")
	val customer: CustomerEdit? = null
)

data class CustomerEdit(

	@field:SerializedName("resume")
	val resume: Any? = null,

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("isCustomer")
	val isCustomer: Boolean? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("profile")
	val profile: Any? = null,

	@field:SerializedName("isAdmin")
	val isAdmin: Boolean? = null,

	@field:SerializedName("job")
	val job: Any? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
