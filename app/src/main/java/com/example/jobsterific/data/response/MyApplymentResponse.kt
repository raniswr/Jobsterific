package com.example.jobsterific.data

import com.google.gson.annotations.SerializedName

data class MyApplymentResponse(

	@field:SerializedName("applyment")
	val applyment: List<ApplymentItem?>? = null
)

data class Batch(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("campaignDesc")
	val campaignDesc: String? = null,

	@field:SerializedName("endDate")
	val endDate: String? = null,

	@field:SerializedName("predict")
	val predict: String? = null,

	@field:SerializedName("batchId")
	val batchId: Int? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("campaignName")
	val campaignName: String? = null,

	@field:SerializedName("campaignKeyword")
	val campaignKeyword: String? = null,

	@field:SerializedName("startDate")
	val startDate: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class ApplymentItem(

	@field:SerializedName("applyId")
	val applyId: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("batch")
	val batch: Batch? = null,

	@field:SerializedName("batchId")
	val batchId: Int? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
