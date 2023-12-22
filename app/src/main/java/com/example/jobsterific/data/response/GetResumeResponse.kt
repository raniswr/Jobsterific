package com.example.jobsterific.data.response

import com.google.gson.annotations.SerializedName

data class GetResumeResponse(

	@field:SerializedName("resume")
	val resume: String? = null
)
