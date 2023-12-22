package com.example.jobsterific.data.response

import com.google.gson.annotations.SerializedName

data class GetAllBatchResponse(

	@field:SerializedName("batches")
	val batches: List<BatchesItem>
)

data class BatchesItem(

	@field:SerializedName("campaignPeriod")
	val campaignPeriod: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("campaignDesc")
	val campaignDesc: String,

	@field:SerializedName("endDate")
	val endDate: String,

	@field:SerializedName("predict")
	val predict: String,

	@field:SerializedName("batchId")
	val batchId: Int,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("campaignName")
	val campaignName: String,

	@field:SerializedName("campaignKeyword")
	val campaignKeyword: String,

	@field:SerializedName("startDate")
	val startDate: String,

	@field:SerializedName("status")
	val status: Boolean,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
