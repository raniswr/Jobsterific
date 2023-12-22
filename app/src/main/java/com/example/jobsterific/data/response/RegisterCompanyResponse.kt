package com.example.jobsterific.data.response

import com.google.gson.annotations.SerializedName

data class RegisterCompanyResponse(

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("website")
	val website: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("sex")
	val sex: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("userId")
	val userId: Any? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("isCustomer")
	val isCustomer: Boolean? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("predict")
	val predict: Predict? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class Predict(

	@field:SerializedName("agriculture")
	val agriculture: Any? = null,

	@field:SerializedName("automobile")
	val automobile: Any? = null,

	@field:SerializedName("businessanalyst")
	val businessanalyst: Any? = null,

	@field:SerializedName("accountant")
	val accountant: Any? = null,

	@field:SerializedName("consultant")
	val consultant: Any? = null,

	@field:SerializedName("javadeveloper")
	val javadeveloper: Any? = null,

	@field:SerializedName("chef")
	val chef: Any? = null,

	@field:SerializedName("aviation")
	val aviation: Any? = null,

	@field:SerializedName("engineering")
	val engineering: Any? = null,

	@field:SerializedName("designer")
	val designer: Any? = null
)
