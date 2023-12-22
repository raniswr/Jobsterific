package com.example.jobsterific.data.response

import com.google.gson.annotations.SerializedName

data class MyBatchResponse(

	@field:SerializedName("batch")
	val batch: List<BatchItem?>? = null
)

data class BatchItem(

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
