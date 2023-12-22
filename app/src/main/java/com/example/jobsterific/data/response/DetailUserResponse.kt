package com.example.jobsterific.data.response

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

	@field:SerializedName("resume")
	val resume: String,

	@field:SerializedName("specifiedUser")
	val specifiedUser: SpecifiedUser
)

data class SpecifiedUser(

	@field:SerializedName("resume")
	val resume: String,

	@field:SerializedName("lastName")
	val lastName: String,

	@field:SerializedName("website")
	val website: Any,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("profile")
	val profile: Any,

	@field:SerializedName("sex")
	val sex: String,

	@field:SerializedName("description")
	val description: Any,

	@field:SerializedName("isAdmin")
	val isAdmin: Boolean,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("firstName")
	val firstName: String,

	@field:SerializedName("isCustomer")
	val isCustomer: Boolean,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("phone")
	val phone: Any,

	@field:SerializedName("predict")
	val predict: String,

	@field:SerializedName("job")
	val job: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("status")
	val status: Boolean,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
