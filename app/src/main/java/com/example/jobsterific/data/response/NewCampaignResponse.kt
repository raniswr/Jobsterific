package com.example.jobsterific.data.response

import com.google.gson.annotations.SerializedName

data class NewCampaignResponse(

	@field:SerializedName("newCampaign")
	val newCampaign: NewCampaign? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class NewCampaign(

	@field:SerializedName("campaignPeriod")
	val campaignPeriod: Any? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("campaignDesc")
	val campaignDesc: String? = null,

	@field:SerializedName("endDate")
	val endDate: String? = null,

	@field:SerializedName("batchId")
	val batchId: Any? = null,

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
